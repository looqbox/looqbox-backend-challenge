package com.leonardo.pokemonapi.sorting

enum class PokemonSortType {
	ALPHABETICAL,
	LENGTH,
	;

	companion object {
		fun fromParam(raw: String?): PokemonSortType {
			if (raw.isNullOrBlank()) {
				return ALPHABETICAL
			}
			return when (raw.trim().uppercase()) {
				"LENGTH" -> LENGTH
				"ALPHABETICAL", "ALPHA" -> ALPHABETICAL
				else -> ALPHABETICAL
			}
		}
	}
}
