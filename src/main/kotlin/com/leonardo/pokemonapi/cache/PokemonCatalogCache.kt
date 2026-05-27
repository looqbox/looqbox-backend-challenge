package com.leonardo.pokemonapi.cache

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicReference

/**
 * Cache em memória da lista completa de nomes da PokéAPI, sem biblioteca de cache (conforme desafio).
 * Thread-safe via [AtomicReference]; TTL verificado a cada leitura.
 */
@Component
class PokemonCatalogCache {

	private val holder = AtomicReference<Entry?>(null)

	private data class Entry(
		val storedAtMillis: Long,
		val names: List<String>,
	)

	/**
	 * @return cópia imutável da lista em cache se ainda válida; senão `null`.
	 */
	fun getValid(ttlMillis: Long): List<String>? {
		val e = holder.get() ?: return null
		val age = System.currentTimeMillis() - e.storedAtMillis
		if (age > ttlMillis) {
			return null
		}
		return e.names
	}

	/**
	 * Lista atualmente armazenada, ignorando TTL (para stale-while-revalidate e métricas).
	 */
	fun peekNamesIgnoringTtl(): List<String>? = holder.get()?.names

	fun hasEntry(): Boolean = holder.get() != null

	fun isExpired(ttlMillis: Long): Boolean {
		val e = holder.get() ?: return false
		return System.currentTimeMillis() - e.storedAtMillis > ttlMillis
	}

	fun entryAgeMillis(): Long? {
		val e = holder.get() ?: return null
		return System.currentTimeMillis() - e.storedAtMillis
	}

	fun cachedNameCount(): Int = holder.get()?.names?.size ?: 0

	fun put(names: List<String>) {
		val snapshot = ArrayList(names)
		holder.set(Entry(System.currentTimeMillis(), snapshot))
	}
}
