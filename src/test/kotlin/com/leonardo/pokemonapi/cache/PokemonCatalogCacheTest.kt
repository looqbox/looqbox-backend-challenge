package com.leonardo.pokemonapi.cache

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PokemonCatalogCacheTest {

	private val cache = PokemonCatalogCache()

	@Test
	fun `empty cache has no entry and zero count`() {
		assertThat(cache.hasEntry()).isFalse()
		assertThat(cache.cachedNameCount()).isZero()
		assertThat(cache.peekNamesIgnoringTtl()).isNull()
		assertThat(cache.getValid(Long.MAX_VALUE)).isNull()
		assertThat(cache.isExpired(0)).isFalse()
		assertThat(cache.entryAgeMillis()).isNull()
	}

	@Test
	fun `put then getValid returns snapshot while within ttl`() {
		cache.put(listOf("a", "b"))
		assertThat(cache.getValid(Long.MAX_VALUE)).containsExactly("a", "b")
		assertThat(cache.hasEntry()).isTrue()
		assertThat(cache.cachedNameCount()).isEqualTo(2)
		assertThat(cache.peekNamesIgnoringTtl()).containsExactly("a", "b")
	}

	@Test
	fun `getValid returns null when older than ttl`() {
		cache.put(listOf("x"))
		Thread.sleep(25)
		assertThat(cache.getValid(10)).isNull()
	}

	@Test
	fun `peek still returns stale after ttl miss`() {
		cache.put(listOf("stale"))
		Thread.sleep(25)
		assertThat(cache.getValid(10)).isNull()
		assertThat(cache.peekNamesIgnoringTtl()).containsExactly("stale")
	}

	@Test
	fun `isExpired true when age exceeds ttl`() {
		cache.put(listOf("y"))
		Thread.sleep(25)
		assertThat(cache.isExpired(10)).isTrue()
	}

	@Test
	fun `entryAgeMillis present after put`() {
		cache.put(listOf("z"))
		assertThat(cache.entryAgeMillis()).isNotNull().isGreaterThanOrEqualTo(0L)
	}

	@Test
	fun `put copies list so external mutation does not change cache`() {
		val mutable = mutableListOf("a")
		cache.put(mutable)
		mutable[0] = "changed"
		assertThat(cache.peekNamesIgnoringTtl()).containsExactly("a")
	}
}
