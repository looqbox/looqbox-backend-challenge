package com.leonardo.pokemonapi.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class RootController {

	@GetMapping("/", produces = [MediaType.TEXT_PLAIN_VALUE])
	fun root(): Mono<String> = Mono.just("Pokemon API is running.")
}
