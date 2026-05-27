package com.leonardo.pokemonapi.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PokemonHighlightMapperTest {

	private val mapper = PokemonHighlightMapper()

	@Test
	fun `blank query does not wrap with pre tags`() {
		val response = mapper.toResponse(listOf("pikachu"), "")
		assertThat(response.result).singleElement()
			.satisfies({ item ->
				assertThat(item.name).isEqualTo("pikachu")
				assertThat(item.highlight).isEqualTo("pikachu")
			})
	}

	@Test
	fun `blank query with whitespace only`() {
		val response = mapper.toResponse(listOf("a"), "   ")
		assertThat(response.result[0].highlight).isEqualTo("a")
	}

	@Test
	fun `highlights first match case insensitively`() {
		val response = mapper.toResponse(listOf("Pikachu"), "pi")
		assertThat(response.result[0].highlight).isEqualTo("<pre>Pi</pre>kachu")
	}

	@Test
	fun `highlights all occurrences in name`() {
		val response = mapper.toResponse(listOf("pipipi"), "pi")
		assertThat(response.result[0].highlight)
			.isEqualTo("<pre>pi</pre><pre>pi</pre><pre>pi</pre>")
	}

	@Test
	fun `escapes regex special characters in query`() {
		val response = mapper.toResponse(listOf("a.c"), ".")
		assertThat(response.result[0].highlight).isEqualTo("a<pre>.</pre>c")
	}

	@Test
	fun `maps multiple pokemons`() {
		val response = mapper.toResponse(listOf("a", "b"), "x")
		assertThat(response.result).hasSize(2)
		assertThat(response.result.map { it.name }).containsExactly("a", "b")
	}
}
