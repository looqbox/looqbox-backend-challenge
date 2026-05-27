package com.leonardo.pokemonapi.service

import com.leonardo.pokemonapi.dto.response.PokemonListResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PokemonListSearchService(
	private val pokemonFetchService: PokemonFetchService,
	private val pokemonFilterService: PokemonFilterService,
	private val pokemonSortService: PokemonSortService,
	private val pokemonResponseService: PokemonResponseService,
) {

	fun listPokemons(
		query: String?,
		sort: String?,
	): Mono<PokemonListResponse> =
		pokemonFetchService.fetchAllNames().map { names ->
			val filtered = pokemonFilterService.filterByQuery(names, query)
			val sorted = pokemonSortService.sort(filtered, sort)
			pokemonResponseService.toListResponse(sorted)
		}
}
