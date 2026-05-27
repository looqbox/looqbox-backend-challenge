package com.leonardo.pokemonapi.service

import com.leonardo.pokemonapi.dto.response.PokemonHighlightResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PokemonHighlightSearchService(
	private val pokemonFetchService: PokemonFetchService,
	private val pokemonFilterService: PokemonFilterService,
	private val pokemonSortService: PokemonSortService,
	private val pokemonResponseService: PokemonResponseService,
) {

	fun highlightPokemons(
		query: String?,
		sort: String?,
	): Mono<PokemonHighlightResponse> =
		pokemonFetchService.fetchAllNames().map { names ->
			val filtered = pokemonFilterService.filterByQuery(names, query)
			val sorted = pokemonSortService.sort(filtered, sort)
			pokemonResponseService.toHighlightResponse(sorted, query)
		}
}
