package com.leonardo.pokemonapi.client

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class PokeApiClient(
	private val webClient: WebClient,
) {

	fun fetchAllPokemonNames(): Mono<List<String>> =
		webClient
			.get()
			.uri("https://pokeapi.co/api/v2/pokemon?limit=100000&offset=0")
			.retrieve()
			.bodyToMono(JsonNode::class.java)
			.switchIfEmpty(Mono.error(IllegalStateException("Resposta vazia da PokéAPI")))
			.map(::toNameList)

	private fun toNameList(json: JsonNode): List<String> {
		val results = json.path("results")
		if (!results.isArray || results.size() == 0) {
			return emptyList()
		}
		return results
			.elements()
			.asSequence()
			.map { it.path("name").asText("") }
			.filter { it.isNotBlank() }
			.toList()
	}
}
