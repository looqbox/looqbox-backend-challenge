package com.looqbox.challenge.sorting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LengthSortingStrategyTest {

    LengthSortingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new LengthSortingStrategy();
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
        void testShouldSortByLengthAscending() {
            List<String> result = strategy.sort(List.of("pikachu", "mew", "bulbasaur"));

            assertThat(result).containsExactly("mew", "pikachu", "bulbasaur");
        }

        @Test
        void testShouldUseAlphabeticalAsTiebreakerForSameLength() {
            List<String> result = strategy.sort(List.of("mew", "ash", "bro"));

            assertThat(result).containsExactly("ash", "bro", "mew");
        }

        @Test
        void testShouldHandleAllSameLength() {
            List<String> result = strategy.sort(List.of("cat", "bat", "ant"));

            assertThat(result).containsExactly("ant", "bat", "cat");
        }

        @Test
        void testShouldSortMixedLengthsCorrectly() {
            List<String> result = strategy.sort(List.of("pikachu", "mew", "charmander", "ash", "bulbasaur"));

            assertThat(result).containsExactly("ash", "mew", "pikachu", "bulbasaur", "charmander");
        }
    }
}
