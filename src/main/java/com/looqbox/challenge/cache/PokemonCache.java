package com.looqbox.challenge.cache;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Cache manual em memória para a lista completa de nomes de Pokémon.
 *
 * Gargalo: Buscar todos os Pokémon da PokéAPI a cada requisição HTTP externa, causaria
 * alta latência. Este cache armazena o resultado na memória e o atualiza somente após o TTL expirar.
 *
 * Solução: Na primeira requisição, busque todos os nomes e armazene-os aqui.
 * Requisições subsequentes dentro do período de TTL são atendidas diretamente da memória
 * sem qualquer chamada de rede, reduzindo o tempo de resposta.
 *
 * O TTL está definido para 1 hora. Se dados em tempo real forem necessários, o TTL pode ser reduzido
 * ou um mecanismo de invalidação explícito pode ser adicionado.
 *
 * Thread-safety: utiliza ReentrantReadWriteLock para garantir que isValid(), getPokemons()
 * e setPokemons() operem de forma atômica e consistente entre threads concorrentes.
 */
@Component
public class PokemonCache {

    private static final long TTL_MS = 3_600_000L; // 1 hora

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private List<String> cachedPokemons;
    private long lastFetchTimeMs;

    public PokemonCache() {
        this.lastFetchTimeMs = 0;
    }

    /**
     * Verifica se o cache ainda é válido com base no TTL.
     *
     * @return {@code true} se o cache estiver válido, {@code false} caso contrário.
     */
    public boolean isValid() {
        lock.readLock().lock();
        try {
            return cachedPokemons != null
                    && (System.currentTimeMillis() - lastFetchTimeMs) < TTL_MS;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Retorna a lista de Pokémon armazenada no cache.
     *
     * @return Lista de nomes de Pokémon.
     */
    public List<String> getPokemons() {
        lock.readLock().lock();
        try {
            return cachedPokemons;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Atualiza o cache com uma nova lista de Pokémon.
     *
     * @param pokemons Lista de nomes de Pokémon a ser armazenada no cache.
     */
    public void setPokemons(List<String> pokemons) {
        lock.writeLock().lock();
        try {
            this.cachedPokemons = List.copyOf(pokemons);
            this.lastFetchTimeMs = System.currentTimeMillis();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
