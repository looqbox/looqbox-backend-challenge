package com.leonardo.pokemonapi.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PokemonFilterServiceTest {

	private val service = PokemonFilterService()

	@Test
	fun `returns full list when query is null`() {
		val names = listOf("pikachu", "eevee")
		assertThat(service.filterByQuery(names, null)).isEqualTo(names)
	}

	@Test
	fun `returns full list when query is blank after trim`() {
		val names = listOf("a", "b")
		assertThat(service.filterByQuery(names, "   ")).isEqualTo(names)
		assertThat(service.filterByQuery(names, "")).isEqualTo(names)
	}

	@Test
	fun `trims query before filtering`() {
		val names = listOf("pikachu")
		assertThat(service.filterByQuery(names, "  ka  ")).containsExactly("pikachu")
	}

	@Test
	fun `filter is case insensitive`() {
		val names = listOf("Pikachu", "EEVEE", "Snorlax")
		assertThat(service.filterByQuery(names, "PI")).containsExactly("Pikachu")
	}

	@Test
	fun `matches any substring in name`() {
		val names = listOf("pidgey", "magikarp", "pidgeotto")
		assertThat(service.filterByQuery(names, "pid")).containsExactly("pidgey", "pidgeotto")
	}

	@Test
	fun `returns empty when no match`() {
		assertThat(service.filterByQuery(listOf("a", "b"), "zzz")).isEmpty()
	}
}
