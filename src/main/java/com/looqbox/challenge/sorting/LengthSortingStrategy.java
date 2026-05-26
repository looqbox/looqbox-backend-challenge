package com.looqbox.challenge.sorting;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Ordena os nomes de Pokémon por comprimento em ordem crescente.
 * OBS: Quando dois nomes têm o mesmo comprimento, a ordem alfabética é usada como critério de desempate
 * para garantir um resultado determinístico.
 *
 * Critério principal: nomes mais curtos vêm primeiro (em ordem crescente de comprimento)
 * Critério de desempate: ordem alfabética para nomes de mesmo comprimento
 */
@Component
public class LengthSortingStrategy implements SortingStrategy {

    @Override
    public List<String> merge(List<String> left, List<String> right) {
        List<String> result = new ArrayList<>(left.size() + right.size());
        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            int leftLen = left.get(i).length();
            int rightLen = right.get(j).length();

            boolean pickLeft = leftLen < rightLen
                    || (leftLen == rightLen && left.get(i).compareTo(right.get(j)) <= 0);

            if (pickLeft) {
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
