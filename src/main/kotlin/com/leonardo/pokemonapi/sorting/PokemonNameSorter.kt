package com.leonardo.pokemonapi.sorting

import org.springframework.stereotype.Component

@Component
class PokemonNameSorter(
	private val alphabeticalPokemonNameSorter: AlphabeticalPokemonNameSorter,
	private val lengthPokemonNameSorter: LengthPokemonNameSorter,
) {

	fun sort(
		names: List<String>,
		type: PokemonSortType,
	): List<String> =
		when (type) {
			PokemonSortType.ALPHABETICAL ->
				alphabeticalPokemonNameSorter.sort(names)
			PokemonSortType.LENGTH ->
				lengthPokemonNameSorter.sort(names)
		}
}
