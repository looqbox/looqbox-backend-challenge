package com.leonardo.pokemonapi.exception

/**
 * Domínio 3 — Pós-fetch síncrono (filtro, ordenação, mappers): falhas inesperadas ou esgotamento de memória.
 */
sealed class PokemonDataProcessingException(
	message: String,
	cause: Throwable? = null,
) : RuntimeException(message, cause) {

	class UnexpectedFailure(cause: Throwable) :
		PokemonDataProcessingException("Erro ao filtrar, ordenar ou mapear a lista de nomes.", cause)

	class ResourceExhausted(cause: OutOfMemoryError) :
		PokemonDataProcessingException("Memória insuficiente para processar a lista completa.", cause)

	companion object {
		fun from(cause: Throwable): PokemonDataProcessingException =
			when (cause) {
				is PokemonDataProcessingException -> cause
				is OutOfMemoryError -> ResourceExhausted(cause)
				else -> UnexpectedFailure(cause)
			}
	}
}
