package com.looqbox.challenge.sorting;

import java.util.ArrayList;
import java.util.List;

/**
 * Design Pattern: Strategy
 *
 * Interface para ordenar uma lista de nomes de Pokémon.
 *
 * Algoritmo: Merge Sort
 *
 * Complexidade de tempo: θ(n log n) para todos os casos (melhor, médio e pior).
 * Complexidade de espaço: θ(n) - listas auxiliares alocadas a cada etapa de merge.
 *
 * Como funciona:
 *   1. Divide: a lista é dividida ao meio a cada nível recursivo,
 *      produzindo θ(log n) níveis de recursão.
 *   2. Conquer: cada metade é ordenada recursivamente até o caso base
 *      de tamanho 0 ou 1 (já ordenado por definição).
 *   3. Merge: as duas metades ordenadas são mescladas percorrendo
 *      cada elemento exatamente uma vez - θ(n) trabalho por nível.
 *
 * Resolução: θ(log n) níveis × θ(n) trabalho por nível = θ(n log n)
 */
public interface SortingStrategy {

    /**
     * Ordena uma lista de nomes de Pokémon usando a estratégia específica.
     * @param pokemons Lista de nomes de Pokémon
     * @return Lista ordenada
     */
    default List<String> sort(List<String> pokemons) {
        if (pokemons == null || pokemons.size() <= 1) {
            return pokemons == null ? new ArrayList<>() : new ArrayList<>(pokemons);
        }

        // Trabalhar em uma cópia para evitar mutar a lista de entrada
        return mergeSort(new ArrayList<>(pokemons));
    }

    /**
     * Divide recursivamente a lista até o caso base, depois mescla de volta em ordem
     * @param list Lista a ser ordenada
     * @return Lista ordenada
     */
    default List<String> mergeSort(List<String> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;

        // 1. Divider: dividir em metades (esquerda e direita).
        List<String> left = mergeSort(new ArrayList<>(list.subList(0, mid)));
        List<String> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())));

        // 2. Conquer: junta as duas metades já ordenadas
        return merge(left, right);
    }

    /**
     * 3. Merge: Mescla duas listas ordenadas em uma lista ordenada
     * @param left Metade esquerda ordenada
     * @param right Metade direita ordenada
     * @return Lista mesclada e ordenada
     */
    List<String> merge(List<String> left, List<String> right);
}
