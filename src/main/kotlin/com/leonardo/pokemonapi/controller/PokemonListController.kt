package com.leonardo.pokemonapi.controller

import com.leonardo.pokemonapi.dto.response.PokemonListResponse
import com.leonardo.pokemonapi.service.PokemonListSearchService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class PokemonListController(
	private val pokemonListSearchService: PokemonListSearchService,
) {

	@GetMapping("/pokemons", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun list(
		@RequestParam(required = false) query: String?,
		@RequestParam(required = false) sort: String?,
	): Mono<PokemonListResponse> = pokemonListSearchService.listPokemons(query, sort)
}
