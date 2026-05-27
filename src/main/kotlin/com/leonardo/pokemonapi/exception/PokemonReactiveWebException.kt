package com.leonardo.pokemonapi.exception

/**
 * Domínio 7 — Exposição HTTP WebFlux (controllers): erros do [Mono] que chegam ao cliente sem mapeamento específico.
 */
class PokemonReactiveWebException(
	message: String = "Erro não tratado no pipeline reativo do endpoint.",
	cause: Throwable,
) : RuntimeException(message, cause) {

	companion object {
		fun fromEndpointPipeline(cause: Throwable): PokemonReactiveWebException =
			PokemonReactiveWebException(cause = cause)
	}
}
