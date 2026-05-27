package com.leonardo.pokemonapi.exception

/**
 * Domínio 4 — Armazenamento em memória do catálogo ([PokemonCatalogCache]): invariantes violadas.
 */
sealed class PokemonCatalogStorageException(
	message: String,
	cause: Throwable? = null,
) : RuntimeException(message, cause) {

	class IllegalNullSnapshot(cause: NullPointerException) :
		PokemonCatalogStorageException("Dados nulos não são permitidos no cache do catálogo.", cause)

	class InconsistentState(message: String, cause: Throwable? = null) :
		PokemonCatalogStorageException(message, cause)

	companion object {
		fun from(cause: Throwable): PokemonCatalogStorageException =
			when (cause) {
				is PokemonCatalogStorageException -> cause
				is NullPointerException -> IllegalNullSnapshot(cause)
				else -> InconsistentState("Estado de cache inválido.", cause)
			}
	}
}
