package com.leonardo.pokemonapi.sorting

/**
 * Comparações manuais entre nomes (sem [java.util.Comparator]).
 */
internal object PokemonNameComparisons {

	fun compareAlphabeticalIgnoreCase(a: String, b: String): Int {
		val lenA = a.length
		val lenB = b.length
		val minLen = if (lenA < lenB) lenA else lenB
		var idx = 0
		while (idx < minLen) {
			val ca = asciiLower(a[idx])
			val cb = asciiLower(b[idx])
			if (ca != cb) {
				return if (ca < cb) -1 else 1
			}
			idx++
		}
		if (lenA < lenB) return -1
		if (lenA > lenB) return 1
		return 0
	}

	fun compareLengthThenAlphabetical(a: String, b: String): Int {
		val lenA = a.length
		val lenB = b.length
		if (lenA < lenB) return -1
		if (lenA > lenB) return 1
		return compareAlphabeticalIgnoreCase(a, b)
	}

	private fun asciiLower(c: Char): Char {
		val code = c.code
		if (code in 65..90) {
			return (code + 32).toChar()
		}
		return c
	}
}
