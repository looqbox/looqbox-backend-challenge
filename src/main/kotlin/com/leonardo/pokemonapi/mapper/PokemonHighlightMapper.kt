package com.leonardo.pokemonapi.mapper

import com.leonardo.pokemonapi.dto.response.PokemonHighlightItem
import com.leonardo.pokemonapi.dto.response.PokemonHighlightResponse
import org.springframework.stereotype.Component

@Component
class PokemonHighlightMapper {

	fun toResponse(
		pokemons: List<String>,
		query: String,
	): PokemonHighlightResponse {
		val result =
			pokemons.map { pokemon ->
				val highlighted =
					if (query.isBlank()) {
						pokemon
					} else {
						Regex(Regex.escape(query), RegexOption.IGNORE_CASE)
							.replace(pokemon) { "<pre>${it.value}</pre>" }
					}
				PokemonHighlightItem(
					name = pokemon,
					highlight = highlighted,
				)
			}

		return PokemonHighlightResponse(
			result = result,
		)
	}
}
