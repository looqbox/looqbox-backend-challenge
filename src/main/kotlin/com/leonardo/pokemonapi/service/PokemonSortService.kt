package com.leonardo.pokemonapi.service

import com.leonardo.pokemonapi.sorting.PokemonNameSorter
import com.leonardo.pokemonapi.sorting.PokemonSortType
import org.springframework.stereotype.Service

@Service
class PokemonSortService(
	private val pokemonNameSorter: PokemonNameSorter,
) {

	fun sort(
		names: List<String>,
		sort: String?,
	): List<String> =
		pokemonNameSorter.sort(names, PokemonSortType.fromParam(sort))
}
