package com.leonardo.pokemonapi.exception

/**
 * Domínio 2 — Obtenção da lista completa ([PokemonFetchService]): falha remota quando não há cache para servir (stale).
 */
class PokemonCatalogFetchException(
	message: String = "Não foi possível obter os nomes na PokéAPI e não existe cache utilizável.",
	cause: Throwable,
) : RuntimeException(message, cause) {

	companion object {
		fun whenRemoteFailsWithoutStale(cause: Throwable): PokemonCatalogFetchException =
			PokemonCatalogFetchException(cause = cause)
	}
}
