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
 * Atualiza o cache em background em intervalos de aproximadamente (TTL − 1 min),
 * para reduzir a chance de expirar antes de haver dados frescos.
 * Usa apenas [java.util.concurrent] (sem biblioteca de agendamento).
 */
@Component
class CachePreRefreshScheduler(
	private val pokeApiClient: PokeApiClient,
	private val pokemonCatalogCache: PokemonCatalogCache,
	@Value("\${pokemon.catalog-cache-ttl-millis:3600000}") private val ttlMillis: Long,
	@Value("\${pokemon.cache-pre-refresh-initial-delay-millis:10000}") private val initialDelayMillis: Long,
) {

	private val log = LoggerFactory.getLogger(javaClass)

	private val executor =
		Executors.newSingleThreadScheduledExecutor { runnable ->
			Thread(runnable, "pokemon-cache-pre-refresh").apply { isDaemon = true }
		}

	@PostConstruct
	fun start() {
		val periodMillis = (ttlMillis - 60_000L).coerceAtLeast(60_000L)
		executor.scheduleAtFixedRate(
			{ refreshSafely() },
			initialDelayMillis.coerceAtLeast(1L),
			periodMillis,
			TimeUnit.MILLISECONDS,
		)
		log.info(
			"CachePreRefreshScheduler iniciado: initialDelay={}ms, period={}ms",
			initialDelayMillis,
			periodMillis,
		)
	}

	private fun refreshSafely() {
		runCatching {
			pokeApiClient.fetchAllPokemonNames()
				.subscribeOn(Schedulers.boundedElastic())
				.subscribe(
					{ names -> pokemonCatalogCache.put(names) },
					{ err -> log.warn("Pré-refresh do cache falhou: {}", err.message) },
				)
		}.onFailure { err ->
			log.warn("Pré-refresh do cache falhou (execução): {}", err.message)
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
