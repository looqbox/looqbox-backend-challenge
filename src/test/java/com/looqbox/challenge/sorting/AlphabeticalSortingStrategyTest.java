package com.looqbox.challenge.sorting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AlphabeticalSortingStrategyTest {

    AlphabeticalSortingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new AlphabeticalSortingStrategy();
    }

    @Nested
    class SortTest {

        @Test
        void testShouldReturnEmptyListWhenInputIsEmpty() {
            List<String> result = strategy.sort(new ArrayList<>());

            assertThat(result).isEmpty();
        }

        @Test
        void testShouldReturnSingleElementUnchanged() {
            List<String> result = strategy.sort(List.of("pikachu"));

            assertThat(result).containsExactly("pikachu");
        }

        @Test
        void testShouldSortAlphabetically() {
            List<String> result = strategy.sort(List.of("pikachu", "bulbasaur", "charmander"));

            assertThat(result).containsExactly("bulbasaur", "charmander", "pikachu");
        }

        @Test
        void testShouldHandleAlreadySortedList() {
            List<String> result = strategy.sort(List.of("bulbasaur", "charmander", "pikachu"));

            assertThat(result).containsExactly("bulbasaur", "charmander", "pikachu");
        }

        @Test
        void testShouldHandleReverseSortedList() {
            List<String> result = strategy.sort(List.of("pikachu", "charmander", "bulbasaur"));

            assertThat(result).containsExactly("bulbasaur", "charmander", "pikachu");
        }

        @Test
        void testShouldSortLargerDataset() {
            List<String> input = List.of("squirtle", "pikachu", "mewtwo", "bulbasaur", "charmander", "jigglypuff");

            List<String> result = strategy.sort(input);

            assertThat(result).containsExactly("bulbasaur", "charmander", "jigglypuff", "mewtwo", "pikachu", "squirtle");
        }

        @Test
        void testShouldNotMutateInputList() {
            List<String> input = new ArrayList<>(List.of("pikachu", "bulbasaur", "charmander"));
            List<String> snapshot = new ArrayList<>(input);

            strategy.sort(input);

            assertThat(input).isEqualTo(snapshot);
        }
    }
}
