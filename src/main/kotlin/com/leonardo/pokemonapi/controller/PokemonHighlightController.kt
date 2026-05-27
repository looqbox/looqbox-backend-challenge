package com.leonardo.pokemonapi.controller

import com.leonardo.pokemonapi.dto.response.PokemonHighlightResponse
import com.leonardo.pokemonapi.service.PokemonHighlightSearchService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class PokemonHighlightController(
	private val pokemonHighlightSearchService: PokemonHighlightSearchService,
) {

	@GetMapping("/pokemons/highlight", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun highlight(
		@RequestParam(required = false) query: String?,
		@RequestParam(required = false) sort: String?,
	): Mono<PokemonHighlightResponse> =
		pokemonHighlightSearchService.highlightPokemons(query, sort)
}
