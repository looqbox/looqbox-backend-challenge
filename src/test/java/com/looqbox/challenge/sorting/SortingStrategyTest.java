package com.looqbox.challenge.sorting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SortingStrategyTest {

    SortingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = mock(SortingStrategy.class, CALLS_REAL_METHODS);
        lenient().doReturn(List.of()).when(strategy).merge(any(), any());
    }

    @Nested
    class SortTest {

        @Test
        void testShouldReturnEmptyListWhenInputIsNull() {
            List<String> result = strategy.sort(null);

            assertThat(result).isEmpty();
        }

        @Test
        void testShouldReturnNewListInstanceWhenInputIsEmpty() {
            List<String> input = new ArrayList<>();

            List<String> result = strategy.sort(input);

            assertThat(result)
                .isNotSameAs(input)
                .isEmpty();
        }

        @Test
        void testShouldReturnNewListInstanceWhenInputHasSingleElement() {
            List<String> input = new ArrayList<>(List.of("pikachu"));

            List<String> result = strategy.sort(input);

            assertThat(result)
                .isNotSameAs(input)
                .containsExactly("pikachu");
        }

        @Test
        void testShouldDelegateSortingToMerge() {
            strategy.sort(List.of("pikachu", "bulbasaur", "charmander"));

            verify(strategy, times(2)).merge(any(), any());
        }

        @Test
        void testShouldNotMutateInputList() {
            List<String> input = new ArrayList<>(List.of("pikachu", "bulbasaur"));
            List<String> snapshot = new ArrayList<>(input);

            strategy.sort(input);

            assertThat(input).isEqualTo(snapshot);
        }
    }

    @Nested
    class MergeSortTest {

        @Test
        void testShouldReturnSameInstanceForSingleElement() {
            List<String> input = new ArrayList<>(List.of("pikachu"));

            List<String> result = strategy.mergeSort(input);

            assertThat(result).isSameAs(input);
        }

        @Test
        void testShouldSplitAndCallMergeForTwoElements() {
            strategy.mergeSort(new ArrayList<>(List.of("pikachu", "bulbasaur")));

            verify(strategy).merge(List.of("pikachu"), List.of("bulbasaur"));
        }

        @Test
        void testShouldRecursivelySplitAndCallMergeForMultipleElements() {
            strategy.mergeSort(new ArrayList<>(List.of("pikachu", "bulbasaur", "charmander")));

            InOrder inOrder = inOrder(strategy);
            inOrder.verify(strategy).merge(List.of("bulbasaur"), List.of("charmander"));
            inOrder.verify(strategy).merge(List.of("pikachu"), List.of());
        }
    }
}
