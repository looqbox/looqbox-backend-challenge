package com.leonardo.pokemonapi.sorting

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PokemonNameSorterTest {

	private val sorter = PokemonNameSorter(
		AlphabeticalPokemonNameSorter(),
		LengthPokemonNameSorter(),
	)

	private val names = listOf("cc", "b", "aa")

	@Test
	fun `uses alphabetical sorter`() {
		assertThat(sorter.sort(names, PokemonSortType.ALPHABETICAL))
			.containsExactly("aa", "b", "cc")
	}

	@Test
	fun `uses length sorter`() {
		assertThat(sorter.sort(names, PokemonSortType.LENGTH))
			.containsExactly("b", "aa", "cc")
	}
}
