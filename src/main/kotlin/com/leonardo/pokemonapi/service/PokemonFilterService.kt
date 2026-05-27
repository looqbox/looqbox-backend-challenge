package com.leonardo.pokemonapi.service

import org.springframework.stereotype.Service

@Service
class PokemonFilterService {

	fun filterByQuery(
		names: List<String>,
		query: String?,
	): List<String> {
		val q = query?.trim().orEmpty()
		if (q.isEmpty()) {
			return names
		}
		return names.filter { it.contains(q, ignoreCase = true) }
	}
}
