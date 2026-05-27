package com.leonardo.pokemonapi.exception

/**
 * Domínio 5 — Tarefas agendadas (pré-refresh, stale-while-revalidate, métricas): falhas em background.
 */
class PokemonBackgroundJobException(
	val taskName: String,
	message: String,
	cause: Throwable? = null,
) : RuntimeException("[$taskName] $message", cause) {

	companion object {
		const val TASK_PRE_REFRESH = "cache-pre-refresh"
		const val TASK_STALE_REVALIDATE = "stale-while-revalidate"
		const val TASK_CACHE_METRICS = "cache-metrics"

		fun scheduledTaskFailed(taskName: String, cause: Throwable): PokemonBackgroundJobException =
			PokemonBackgroundJobException(taskName, "Falha em tarefa agendada.", cause)
	}
}
