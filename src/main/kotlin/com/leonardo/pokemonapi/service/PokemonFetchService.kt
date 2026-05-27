package com.leonardo.pokemonapi.service

import com.leonardo.pokemonapi.cache.PokemonCatalogCache
import com.leonardo.pokemonapi.client.PokeApiClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PokemonFetchService(
	private val pokeApiClient: PokeApiClient,
	private val pokemonCatalogCache: PokemonCatalogCache,
	@Value("\${pokemon.catalog-cache-ttl-millis:3600000}") private val ttlMillis: Long,
) {

	private val log = LoggerFactory.getLogger(javaClass)

	fun fetchAllNames(): Mono<List<String>> {
		val cached = pokemonCatalogCache.getValid(ttlMillis)
		if (cached != null) {
			return Mono.just(cached)
		}
		val stale = pokemonCatalogCache.peekNamesIgnoringTtl()
		return pokeApiClient.fetchAllPokemonNames()
			.doOnNext { names -> pokemonCatalogCache.put(names) }
			.onErrorResume { error ->
				if (stale != null) {
					log.warn(
						"Fetch da PokéAPI falhou; servindo cache expirado (stale-while-revalidate). Causa: {}",
						error.message,
					)
					Mono.just(stale)
				} else {
					Mono.error(error)
				}
			}
	}
}
