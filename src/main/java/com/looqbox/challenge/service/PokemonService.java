package com.looqbox.challenge.service;

import com.looqbox.challenge.cache.PokemonCache;
import com.looqbox.challenge.client.PokeApiClient;
import com.looqbox.challenge.dto.PokemonHighlightItem;
import com.looqbox.challenge.dto.PokemonHighlightResponse;
import com.looqbox.challenge.dto.PokemonListResponse;
import com.looqbox.challenge.dto.SortType;
import com.looqbox.challenge.sorting.AlphabeticalSortingStrategy;
import com.looqbox.challenge.sorting.LengthSortingStrategy;
import com.looqbox.challenge.sorting.SortingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class PokemonService {

    private final PokeApiClient pokeApiClient;
    private final PokemonCache pokemonCache;
    private final Map<SortType, SortingStrategy> sortingStrategies;

    public PokemonService(PokeApiClient pokeApiClient,
                          PokemonCache pokemonCache,
                          AlphabeticalSortingStrategy alphabeticalSortingStrategy,
                          LengthSortingStrategy lengthSortingStrategy) {
        this.pokeApiClient = pokeApiClient;
        this.pokemonCache = pokemonCache;
        
        this.sortingStrategies = new EnumMap<>(SortType.class);
        this.sortingStrategies.put(SortType.ALPHABETICAL, alphabeticalSortingStrategy);
        this.sortingStrategies.put(SortType.LENGTH, lengthSortingStrategy);
    }

    /**
     * Busca os Pokémon de acordo com o filtro e ordenação especificados.
     * 
     * @param query Filtro para buscar nomes de Pokémon.
     * @param sortType Tipo de ordenação.
     * @return Lista dos nomes dos Pokémon correspondentes ao filtro.
     */
    public PokemonListResponse searchPokemons(String query, SortType sortType) {
        return new PokemonListResponse(getPokemons(query, sortType));
    }

    /**
     * Busca os Pokémon de acordo com o filtro e ordenação especificados, retornando também o termo de
     * busca com destaque.
     * 
     * @param query Filtro para buscar nomes de Pokémon.
     * @param sortType Tipo de ordenação.
     * @return Lista dos nomes dos Pokémon correspondentes ao filtro, com destaque para o termo de busca.
     */
    public PokemonHighlightResponse searchPokemonsWithHighlight(String query, SortType sortType) {
        List<String> pokemons = getPokemons(query, sortType);

        List<PokemonHighlightItem> items = new ArrayList<>(pokemons.size());
        for (String name : pokemons) {
            items.add(new PokemonHighlightItem(name, buildHighlight(name, query)));
        }

        return new PokemonHighlightResponse(items);
    }

    /**
     * Busca todos os Pokémon, aplica o filtro e ordenação.
     * 
     * @param query Filtro para buscar nomes de Pokémon.
     * @param sortType Tipo de ordenação.
     * @return Lista dos nomes dos Pokémon correspondentes ao filtro.
     */
    private List<String> getPokemons(String query, SortType sortType) {
        List<String> all = getAllPokemons();
        List<String> filtered = filterByQuery(all, query);
        return sortingStrategies.get(sortType).sort(filtered);
    }

    /**
     * @return Lista completa de nomes de Pokémon, obtida do cache quando disponível.
     * Em caso de falha no cache, busca os dados na PokéAPI e os preenche.
     */
    private List<String> getAllPokemons() {
        if (pokemonCache.isValid()) {
            return pokemonCache.getPokemons();
        }

        List<String> pokemons = pokeApiClient.fetchAllPokemonNames();
        pokemonCache.setPokemons(pokemons);
        return pokemons;
    }

    /**
     * @param pokemons Lista de nomes de Pokémon a ser filtrada.
     * @param query Substring de busca.
     * 
     * @return Lista para exibir apenas os nomes que contêm a substring da consulta.
     * Se a consulta for nula ou estiver em branco, todos os nomes serão retornados.
     */
    private List<String> filterByQuery(List<String> pokemons, String query) {
        if (!StringUtils.hasText(query)) {
            return new ArrayList<>(pokemons);
        }

        List<String> filtered = new ArrayList<>();

        for (String name : pokemons) {
            if (name.toLowerCase().contains(query.toLowerCase())) {
                filtered.add(name);
            }
        }
        return filtered;
    }

    /**
     * Envolve a primeira ocorrência do termo de consulta no nome do Pokémon com
     * tags {@code <pre>} para destacar a correspondência.
     *
     * A correspondência não diferencia maiúsculas de minúsculas, mas a capitalização original
     * do nome é preservada.
     *
     * Exemplo: name="Pikachu", query="pi" -> "<pre>Pi</pre>kachu"
     * 
     * @return O nome do Pokémon com o termo de consulta destacado.
     */
    private String buildHighlight(String name, String query) {
        if (!StringUtils.hasText(query)) {
            return name;
        }

        int index = name.toLowerCase().indexOf(query.toLowerCase());

        String before = name.substring(0, index);
        String matched = name.substring(index, index + query.length());
        String after = name.substring(index + query.length());
        return before + "<pre>" + matched + "</pre>" + after;
    }
}
