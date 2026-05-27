package com.leonardo.pokemonapi.sorting

/**
 * Quicksort in-place sobre [MutableList], sem APIs de ordenação da stdlib.
 *
 * Pivô: elemento do **meio** do intervalo (mitiga listas já ordenadas com pivô no extremo).
 * Partição estilo Lomuto: elementos menores à esquerda, maiores à direita do pivô final.
 *
 * Visão rápida (leiga):
 * - Escolhemos um "pivô" (um nome de referência).
 * - Reorganizamos a lista para ficar: [menores que o pivô] pivô [maiores que o pivô].
 * - Repetimos o processo recursivamente nos dois lados até tudo ficar ordenado.
 */
internal object ManualQuicksort {

	fun sort(
		arr: MutableList<String>,
		compare: (String, String) -> Int,
	) {
		if (arr.size < 2) {
			return
		}
		// Ordena a lista inteira (do índice 0 ao último) usando a regra de comparação recebida.
		quicksort(arr, 0, arr.size - 1, compare)
	}

	private fun quicksort(
		arr: MutableList<String>,
		low: Int,
		high: Int,
		compare: (String, String) -> Int,
	) {
		if (low >= high) {
			return
		}
		// partition devolve onde o pivô "parou". À esquerda ficam menores, à direita maiores.
		val pivotIndex = partition(arr, low, high, compare)
		// Ordena o lado esquerdo.
		quicksort(arr, low, pivotIndex - 1, compare)
		// Ordena o lado direito.
		quicksort(arr, pivotIndex + 1, high, compare)
	}

	private fun partition(
		arr: MutableList<String>,
		low: Int,
		high: Int,
		compare: (String, String) -> Int,
	): Int {
		// Escolhe pivô no meio para reduzir chance de pior caso em entradas já ordenadas.
		val mid = low + (high - low) / 2
		swap(arr, mid, high)
		val pivot = arr[high]

		// i caminha da esquerda para a direita procurando itens "grandes".
		// j caminha da direita para a esquerda procurando itens "pequenos".
		var i = low
		var j = high - 1
		while (i <= j) {
			// Avança i enquanto o item atual já está do lado correto (menor que o pivô).
			while (compare(arr[i], pivot) < 0) {
				i++
			}
			// Recua j enquanto o item atual já está do lado correto (maior que o pivô).
			while (j >= low && compare(arr[j], pivot) > 0) {
				j--
			}
			if (i <= j) {
				// Trocamos um item "grande" da esquerda com um item "pequeno" da direita.
				swap(arr, i, j)
				i++
				j--
			}
		}
		// Coloca o pivô na posição final. Tudo à esquerda <= pivô e à direita >= pivô.
		swap(arr, i, high)
		return i
	}

	private fun swap(arr: MutableList<String>, a: Int, b: Int) {
		val tmp = arr[a]
		arr[a] = arr[b]
		arr[b] = tmp
	}
}
