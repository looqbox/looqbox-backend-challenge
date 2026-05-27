package com.leonardo.pokemonapi.sorting

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AlphabeticalPokemonNameSorterTest {

	private val sorter = AlphabeticalPokemonNameSorter()

	@Test
	fun `sorts small list ascending ignore case`() {
		val input = listOf("Zapdos", "abra", "Mew")
		assertThat(sorter.sort(input)).containsExactly("abra", "Mew", "Zapdos")
	}

	@Test
	fun `equal names stay stable relative order for ties`() {
		val input = listOf("aa", "AA", "Aa")
		val out = sorter.sort(input)
		assertThat(out).hasSize(3)
		assertThat(out.toSet()).containsExactlyInAnyOrder("aa", "AA", "Aa")
	}

	@Test
	fun `empty and single element`() {
		assertThat(sorter.sort(emptyList())).isEmpty()
		assertThat(sorter.sort(listOf("solo"))).containsExactly("solo")
	}

	@Test
	fun `does not mutate original list`() {
		val input = mutableListOf("b", "a")
		sorter.sort(input)
		assertThat(input).containsExactly("b", "a")
	}
}
