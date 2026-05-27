package com.leonardo.pokemonapi.schedulers

import com.leonardo.pokemonapi.cache.PokemonCatalogCache
import com.leonardo.pokemonapi.client.PokeApiClient
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Enquanto o cache estiver expirado (TTL) mas ainda houver dados, tenta revalidar em background.
 * Em falha, mantém a lista antiga até um fetch bem-sucedido.
 * Usa apenas [java.util.concurrent] (sem biblioteca de agendamento).
 */
@Component
class StaleWhileRevalidateScheduler(
	private val pokeApiClient: PokeApiClient,
	private val pokemonCatalogCache: PokemonCatalogCache,
	@Value("\${pokemon.catalog-cache-ttl-millis:3600000}") private val ttlMillis: Long,
	@Value("\${pokemon.stale-while-revalidate-interval-millis:120000}") private val intervalMillis: Long,
) {

	private val log = LoggerFactory.getLogger(javaClass)

	private val executor =
		Executors.newSingleThreadScheduledExecutor { runnable ->
			Thread(runnable, "pokemon-stale-revalidate").apply { isDaemon = true }
		}

	@PostConstruct
	fun start() {
		executor.scheduleAtFixedRate(
			{ revalidateIfStale() },
			intervalMillis.coerceAtLeast(1L),
			intervalMillis.coerceAtLeast(1L),
			TimeUnit.MILLISECONDS,
		)
		log.info(
			"StaleWhileRevalidateScheduler iniciado: interval={}ms",
			intervalMillis,
		)
	}

	private fun revalidateIfStale() {
		if (!pokemonCatalogCache.hasEntry()) {
			return
		}
		if (!pokemonCatalogCache.isExpired(ttlMillis)) {
			return
		}
		runCatching {
			pokeApiClient.fetchAllPokemonNames()
				.subscribeOn(Schedulers.boundedElastic())
				.subscribe(
					{ names -> pokemonCatalogCache.put(names) },
					{ err -> log.debug("Revalidação em background falhou (mantém stale): {}", err.message) },
				)
		}.onFailure { err ->
			log.debug("Revalidação em background falhou (mantém stale): {}", err.message)
		}
	}

	@PreDestroy
	fun shutdown() {
		executor.shutdown()
		try {
			if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
				executor.shutdownNow()
			}
		} catch (_: InterruptedException) {
			executor.shutdownNow()
			Thread.currentThread().interrupt()
		}
	}
}
