package com.leonardo.pokemonapi.controller

import com.leonardo.pokemonapi.dto.response.PokemonCatalogCacheMetricsResponse
import com.leonardo.pokemonapi.service.PokemonCatalogCacheMetricsService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * Endpoint operacional para inspeção do cache do catálogo (fora do contrato principal do desafio).
 * Em produção, restringir por rede ou autenticação.
 */
@RestController
@RequestMapping("/internal/cache-catalog")
class CacheCatalogMetricsController(
	private val pokemonCatalogCacheMetricsService: PokemonCatalogCacheMetricsService,
) {

	@GetMapping("/metrics", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun getCacheMetrics(): Mono<PokemonCatalogCacheMetricsResponse> =
		Mono.fromCallable { pokemonCatalogCacheMetricsService.snapshot() }
}
