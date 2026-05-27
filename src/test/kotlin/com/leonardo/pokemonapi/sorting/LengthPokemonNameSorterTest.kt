package com.leonardo.pokemonapi.sorting

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LengthPokemonNameSorterTest {

	private val sorter = LengthPokemonNameSorter()

	@Test
	fun `sorts by length ascending then alphabetical for ties`() {
		val input = listOf("cc", "b", "aa")
		assertThat(sorter.sort(input)).containsExactly("b", "aa", "cc")
	}

	@Test
	fun `tie break is case insensitive alphabetical`() {
		val input = listOf("Bb", "aa", "Cc")
		assertThat(sorter.sort(input)).containsExactly("aa", "Bb", "Cc")
	}

	@Test
	fun `mixed lengths and casing`() {
		val input = listOf("PIKA", "x", "Ab")
		assertThat(sorter.sort(input)).containsExactly("x", "Ab", "PIKA")
	}

	@Test
	fun `empty and single`() {
		assertThat(sorter.sort(emptyList())).isEmpty()
		assertThat(sorter.sort(listOf("mewtwo"))).containsExactly("mewtwo")
	}

	@Test
	fun `does not mutate original list`() {
		val input = mutableListOf("aaa", "a")
		sorter.sort(input)
		assertThat(input).containsExactly("aaa", "a")
	}
}
