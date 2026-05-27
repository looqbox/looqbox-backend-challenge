package com.leonardo.pokemonapi.sorting

import org.springframework.stereotype.Component

@Component
class LengthPokemonNameSorter {

	fun sort(names: List<String>): List<String> {
		val copy = names.toMutableList()
		ManualQuicksort.sort(copy, PokemonNameComparisons::compareLengthThenAlphabetical)
		return copy
	}
}
