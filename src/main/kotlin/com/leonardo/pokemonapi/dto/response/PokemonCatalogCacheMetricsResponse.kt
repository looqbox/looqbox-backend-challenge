package com.leonardo.pokemonapi.dto.response

/**
 * Snapshot operacional do [com.leonardo.pokemonapi.cache.PokemonCatalogCache]
 * (mesmos campos que o [com.leonardo.pokemonapi.schedulers.CacheMetricsScheduler] regista em log).
 */
data class PokemonCatalogCacheMetricsResponse(
	val hasEntry: Boolean,
	val nameCount: Int,
	val expired: Boolean,
	val entryAgeMillis: Long?,
	val catalogCacheTtlMillis: Long,
)
