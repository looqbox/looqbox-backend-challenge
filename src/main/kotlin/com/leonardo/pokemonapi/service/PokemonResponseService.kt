package com.leonardo.pokemonapi.service

import com.leonardo.pokemonapi.dto.response.PokemonHighlightResponse
import com.leonardo.pokemonapi.dto.response.PokemonListResponse
import com.leonardo.pokemonapi.mapper.PokemonHighlightMapper
import com.leonardo.pokemonapi.mapper.PokemonListMapper
import org.springframework.stereotype.Service

@Service
class PokemonResponseService(
	private val pokemonListMapper: PokemonListMapper,
	private val pokemonHighlightMapper: PokemonHighlightMapper,
) {

	fun toListResponse(names: List<String>): PokemonListResponse =
		pokemonListMapper.toResponse(names)

	fun toHighlightResponse(
		names: List<String>,
		query: String?,
	): PokemonHighlightResponse =
		pokemonHighlightMapper.toResponse(names, query?.trim().orEmpty())
}
