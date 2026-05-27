package com.leonardo.pokemonapi.sorting

import org.springframework.stereotype.Component

@Component
class AlphabeticalPokemonNameSorter {

	fun sort(names: List<String>): List<String> {
		val copy = names.toMutableList()
		ManualQuicksort.sort(copy, PokemonNameComparisons::compareAlphabeticalIgnoreCase)
		return copy
	}
}
