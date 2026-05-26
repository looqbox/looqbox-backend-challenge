package com.looqbox.challenge.sorting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Ordena os nomes de Pokémon em ordem alfabética crescente.
 * 
 * Critério principal: compara elementos de ambas as metades e sempre escolhe o menor alfabeticamente.
 */
@Component
public class AlphabeticalSortingStrategy implements SortingStrategy {

    @Override
    public List<String> merge(List<String> left, List<String> right) {
        List<String> result = new ArrayList<>(left.size() + right.size());
        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            // String.compareTo retorna negativo quando left[i] < right[j] alfabeticamente
            if (left.get(i).compareTo(right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        // Adiciona os elementos restantes da metade esquerda.
        while (i < left.size()) {
            result.add(left.get(i++));
        }

        // Adiciona os elementos restantes da metade direita.
        while (j < right.size()) {
            result.add(right.get(j++));
        }

        return result;
    }
}
