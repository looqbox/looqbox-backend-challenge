package com.leonardo.pokemonapi.service

import com.leonardo.pokemonapi.sorting.AlphabeticalPokemonNameSorter
import com.leonardo.pokemonapi.sorting.LengthPokemonNameSorter
import com.leonardo.pokemonapi.sorting.PokemonNameSorter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PokemonSortServiceTest {

	private val service = PokemonSortService(
		PokemonNameSorter(
			AlphabeticalPokemonNameSorter(),
			LengthPokemonNameSorter(),
		),
	)

	private val names = listOf("cc", "b", "aa")

	@Test
	fun `delegates to alphabetical when sort param null`() {
		assertThat(service.sort(names, null)).containsExactly("aa", "b", "cc")
	}

	@Test
	fun `delegates to length when sort param LENGTH`() {
		assertThat(service.sort(names, "LENGTH")).containsExactly("b", "aa", "cc")
	}

	@Test
	fun `unknown sort param defaults to alphabetical via PokemonSortType`() {
		assertThat(service.sort(names, "nope")).containsExactly("aa", "b", "cc")
	}
}
