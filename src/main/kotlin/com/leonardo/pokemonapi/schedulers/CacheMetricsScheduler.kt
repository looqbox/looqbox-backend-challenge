package com.leonardo.pokemonapi.schedulers

import com.leonardo.pokemonapi.service.PokemonCatalogCacheMetricsService
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Registra periodicamente métricas simples do cache em log (sem Prometheus obrigatório).
 * Os mesmos valores estão disponíveis em `GET /internal/cache-catalog/metrics` via [PokemonCatalogCacheMetricsService].
 * Usa apenas [java.util.concurrent] (sem biblioteca de agendamento).
 */
@Component
class CacheMetricsScheduler(
	private val pokemonCatalogCacheMetricsService: PokemonCatalogCacheMetricsService,
	@Value("\${pokemon.cache-metrics-interval-millis:300000}") private val intervalMillis: Long,
) {

	private val log = LoggerFactory.getLogger(javaClass)

	private val executor =
		Executors.newSingleThreadScheduledExecutor { runnable ->
			Thread(runnable, "pokemon-cache-metrics").apply { isDaemon = true }
		}

	@PostConstruct
	fun start() {
		executor.scheduleAtFixedRate(
			{ emitMetrics() },
			intervalMillis.coerceAtLeast(1L),
			intervalMillis.coerceAtLeast(1L),
			TimeUnit.MILLISECONDS,
		)
		log.info("CacheMetricsScheduler iniciado: interval={}ms", intervalMillis)
	}

	private fun emitMetrics() {
		val s = pokemonCatalogCacheMetricsService.snapshot()
		log.info(
			"[cache-metrics] hasEntry={} nameCount={} expired={} entryAgeMillis={}",
			s.hasEntry,
			s.nameCount,
			s.expired,
			s.entryAgeMillis,
		)
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
