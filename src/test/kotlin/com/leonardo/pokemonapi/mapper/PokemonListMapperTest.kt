package com.leonardo.pokemonapi.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PokemonListMapperTest {

	private val mapper = PokemonListMapper()

	@Test
	fun `wraps names in result field`() {
		val names = listOf("a", "b")
		val response = mapper.toResponse(names)
		assertThat(response.result).isSameAs(names)
	}

	@Test
	fun `empty list`() {
		assertThat(mapper.toResponse(emptyList()).result).isEmpty()
	}
}
