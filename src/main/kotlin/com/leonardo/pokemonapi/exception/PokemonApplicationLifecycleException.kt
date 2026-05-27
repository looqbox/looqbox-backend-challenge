package com.leonardo.pokemonapi.exception

/**
 * Domínio 6 — Arranque e encerramento da aplicação (Spring / JVM): contexto, porta, shutdown.
 */
sealed class PokemonApplicationLifecycleException(
	message: String,
	cause: Throwable? = null,
) : RuntimeException(message, cause) {

	class ContextInitializationFailed(cause: Throwable) :
		PokemonApplicationLifecycleException("Falha ao iniciar o contexto Spring.", cause)

	class GracefulShutdownInterrupted(cause: InterruptedException) :
		PokemonApplicationLifecycleException("Encerramento ordenado interrompido.", cause)

	companion object {
		fun fromStartupFailure(cause: Throwable): PokemonApplicationLifecycleException =
			ContextInitializationFailed(cause)

		fun fromShutdownInterrupt(cause: InterruptedException): PokemonApplicationLifecycleException =
			GracefulShutdownInterrupted(cause)
	}
}
