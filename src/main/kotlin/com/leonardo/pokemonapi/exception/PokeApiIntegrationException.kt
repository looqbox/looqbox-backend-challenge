package com.leonardo.pokemonapi.exception

import org.springframework.core.codec.DecodingException
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException

/**
 * Domínio 1 — Integração HTTP com a PokéAPI ([WebClient]): corpo vazio, HTTP de erro, rede e payload JSON.
 */
sealed class PokeApiIntegrationException(
	message: String,
	cause: Throwable? = null,
) : RuntimeException(message, cause) {

	class EmptyResponse : PokeApiIntegrationException("Resposta vazia da PokéAPI.")

	class HttpFailure(
		val statusCode: Int,
		cause: WebClientResponseException,
	) : PokeApiIntegrationException("PokéAPI retornou HTTP $statusCode.", cause)

	class NetworkFailure(cause: WebClientRequestException) :
		PokeApiIntegrationException("Falha de rede ao contactar a PokéAPI: ${cause.message}", cause)

	class PayloadDecodingFailure(cause: Throwable) :
		PokeApiIntegrationException("JSON da PokéAPI inválido ou inesperado.", cause)

	companion object {
		private const val EMPTY_BODY_MESSAGE = "Resposta vazia da PokéAPI"

		/** Normaliza falhas do cliente HTTP / codecs para este domínio. */
		fun from(cause: Throwable): PokeApiIntegrationException =
			when (cause) {
				is PokeApiIntegrationException -> cause
				is WebClientResponseException -> HttpFailure(cause.statusCode.value(), cause)
				is WebClientRequestException -> NetworkFailure(cause)
				is DecodingException -> PayloadDecodingFailure(cause)
				is IllegalStateException if cause.message == EMPTY_BODY_MESSAGE -> EmptyResponse()
				else -> PayloadDecodingFailure(cause)
			}
	}
}
