package com.leonardo.pokemonapi.sorting

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ManualQuicksortTest {

	@Test
	fun `sorts alphabetically`() {
		val list = mutableListOf("z", "a", "m")
		ManualQuicksort.sort(list, PokemonNameComparisons::compareAlphabeticalIgnoreCase)
		assertThat(list).containsExactly("a", "m", "z")
	}

	@Test
	fun `sorts by length then name`() {
		val list = mutableListOf("aaa", "b", "cc")
		ManualQuicksort.sort(list, PokemonNameComparisons::compareLengthThenAlphabetical)
		assertThat(list).containsExactly("b", "cc", "aaa")
	}

	@Test
	fun `handles empty and single element`() {
		val empty = mutableListOf<String>()
		ManualQuicksort.sort(empty, PokemonNameComparisons::compareAlphabeticalIgnoreCase)
		assertThat(empty).isEmpty()

		val single = mutableListOf("x")
		ManualQuicksort.sort(single, PokemonNameComparisons::compareAlphabeticalIgnoreCase)
		assertThat(single).containsExactly("x")
	}
}
