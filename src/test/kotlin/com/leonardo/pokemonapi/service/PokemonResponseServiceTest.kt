package com.leonardo.pokemonapi.service

import com.leonardo.pokemonapi.mapper.PokemonHighlightMapper
import com.leonardo.pokemonapi.mapper.PokemonListMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PokemonResponseServiceTest {

	private val service = PokemonResponseService(
		PokemonListMapper(),
		PokemonHighlightMapper(),
	)

	@Test
	fun `toListResponse delegates to list mapper`() {
		val names = listOf("a", "b")
		val response = service.toListResponse(names)
		assertThat(response.result).isSameAs(names)
	}

	@Test
	fun `toHighlightResponse trims query before mapping`() {
		val response = service.toHighlightResponse(listOf("pikachu"), "  pi  ")
		assertThat(response.result.single().highlight).isEqualTo("<pre>pi</pre>kachu")
	}

	@Test
	fun `toHighlightResponse null query uses empty string for mapper`() {
		val response = service.toHighlightResponse(listOf("mew"), null)
		assertThat(response.result.single().highlight).isEqualTo("mew")
	}
}
