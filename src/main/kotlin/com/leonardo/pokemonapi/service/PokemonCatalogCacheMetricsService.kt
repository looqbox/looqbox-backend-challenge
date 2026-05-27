package com.leonardo.pokemonapi.service

import com.leonardo.pokemonapi.cache.PokemonCatalogCache
import com.leonardo.pokemonapi.dto.response.PokemonCatalogCacheMetricsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PokemonCatalogCacheMetricsService(
	private val pokemonCatalogCache: PokemonCatalogCache,
	@Value("\${pokemon.catalog-cache-ttl-millis:3600000}") private val catalogCacheTtlMillis: Long,
) {

	fun snapshot(): PokemonCatalogCacheMetricsResponse =
		PokemonCatalogCacheMetricsResponse(
			hasEntry = pokemonCatalogCache.hasEntry(),
			nameCount = pokemonCatalogCache.cachedNameCount(),
			expired = pokemonCatalogCache.isExpired(catalogCacheTtlMillis),
			entryAgeMillis = pokemonCatalogCache.entryAgeMillis(),
			catalogCacheTtlMillis = catalogCacheTtlMillis,
		)
}
