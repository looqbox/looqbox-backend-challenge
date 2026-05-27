package com.leonardo.pokemonapi.mapper

import com.leonardo.pokemonapi.dto.response.PokemonListResponse
import org.springframework.stereotype.Component

@Component
class PokemonListMapper {

	fun toResponse(pokemons: List<String>): PokemonListResponse =
		PokemonListResponse(
			result = pokemons,
		)
}
