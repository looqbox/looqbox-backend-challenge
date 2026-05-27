package com.leonardo.pokemonapi.sorting

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PokemonSortTypeTest {

	@Test
	fun `fromParam null defaults to alphabetical`() {
		assertThat(PokemonSortType.fromParam(null)).isEqualTo(PokemonSortType.ALPHABETICAL)
	}

	@Test
	fun `fromParam blank defaults to alphabetical`() {
		assertThat(PokemonSortType.fromParam("")).isEqualTo(PokemonSortType.ALPHABETICAL)
		assertThat(PokemonSortType.fromParam("   ")).isEqualTo(PokemonSortType.ALPHABETICAL)
	}

	@Test
	fun `fromParam length`() {
		assertThat(PokemonSortType.fromParam("LENGTH")).isEqualTo(PokemonSortType.LENGTH)
		assertThat(PokemonSortType.fromParam("length")).isEqualTo(PokemonSortType.LENGTH)
		assertThat(PokemonSortType.fromParam("  LeNgTh  ")).isEqualTo(PokemonSortType.LENGTH)
	}

	@Test
	fun `fromParam alphabetical aliases`() {
		assertThat(PokemonSortType.fromParam("ALPHABETICAL")).isEqualTo(PokemonSortType.ALPHABETICAL)
		assertThat(PokemonSortType.fromParam("alpha")).isEqualTo(PokemonSortType.ALPHABETICAL)
		assertThat(PokemonSortType.fromParam("  alphabetical  ")).isEqualTo(PokemonSortType.ALPHABETICAL)
	}

	@Test
	fun `fromParam unknown defaults to alphabetical`() {
		assertThat(PokemonSortType.fromParam("unknown")).isEqualTo(PokemonSortType.ALPHABETICAL)
		assertThat(PokemonSortType.fromParam("123")).isEqualTo(PokemonSortType.ALPHABETICAL)
	}
}
