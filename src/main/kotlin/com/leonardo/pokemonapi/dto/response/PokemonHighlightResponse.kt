package com.leonardo.pokemonapi.dto.response

data class PokemonHighlightResponse(
	val result: List<PokemonHighlightItem>,
)

data class PokemonHighlightItem(
	val name: String,
	val highlight: String,
)
