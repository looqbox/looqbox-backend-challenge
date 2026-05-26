package com.looqbox.challenge.service;

import com.looqbox.challenge.cache.PokemonCache;
import com.looqbox.challenge.client.PokeApiClient;
import com.looqbox.challenge.dto.PokemonHighlightResponse;
import com.looqbox.challenge.dto.PokemonListResponse;
import com.looqbox.challenge.dto.SortType;
import com.looqbox.challenge.sorting.AlphabeticalSortingStrategy;
import com.looqbox.challenge.sorting.LengthSortingStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    PokeApiClient pokeApiClient;

    @Mock
    PokemonCache pokemonCache;

    PokemonService pokemonService;

    InOrder inOrder;
    Object[] allMocks;

    @BeforeEach
    void setUp() {
        allMocks = new Object[]{pokeApiClient, pokemonCache};
        inOrder = Mockito.inOrder(allMocks);
        pokemonService = new PokemonService(
                pokeApiClient,
                pokemonCache,
                new AlphabeticalSortingStrategy(),
                new LengthSortingStrategy()
        );
    }

    @AfterEach
    void tearDown() {
        inOrder.verifyNoMoreInteractions();
        verifyNoMoreInteractions(allMocks);
    }

    @Nested
    class SearchPokemonsTest {

        @Test
        void testShouldReturnAllPokemonsWhenQueryIsNull() {
            when(pokemonCache.isValid()).thenReturn(true);
            when(pokemonCache.getPokemons()).thenReturn(List.of("pikachu", "bulbasaur"));

            PokemonListResponse result = pokemonService.searchPokemons(null, SortType.ALPHABETICAL);

            assertThat(result.getResult()).containsExactly("bulbasaur", "pikachu");
            inOrder.verify(pokemonCache).isValid();
            inOrder.verify(pokemonCache).getPokemons();
        }

        @Test
        void testShouldReturnAllPokemonsWhenQueryIsBlank() {
            when(pokemonCache.isValid()).thenReturn(true);
            when(pokemonCache.getPokemons()).thenReturn(List.of("pikachu", "bulbasaur"));

            PokemonListResponse result = pokemonService.searchPokemons("", SortType.ALPHABETICAL);

            assertThat(result.getResult()).containsExactly("bulbasaur", "pikachu");
            inOrder.verify(pokemonCache).isValid();
            inOrder.verify(pokemonCache).getPokemons();
        }

        @Test
        void testShouldReturnFilteredAndSortedPokemonsAlphabetically() {
            when(pokemonCache.isValid()).thenReturn(true);
            when(pokemonCache.getPokemons()).thenReturn(List.of("raichu", "pikachu", "bulbasaur"));

            PokemonListResponse result = pokemonService.searchPokemons("chu", SortType.ALPHABETICAL);

            assertThat(result.getResult()).containsExactly("pikachu", "raichu");
            inOrder.verify(pokemonCache).isValid();
            inOrder.verify(pokemonCache).getPokemons();
        }

        @Test
        void testShouldReturnFilteredAndSortedPokemonsByLength() {
            when(pokemonCache.isValid()).thenReturn(true);
            when(pokemonCache.getPokemons()).thenReturn(List.of("raichu", "pikachu", "bulbasaur"));

            PokemonListResponse result = pokemonService.searchPokemons("chu", SortType.LENGTH);

            assertThat(result.getResult()).containsExactly("raichu", "pikachu");
            inOrder.verify(pokemonCache).isValid();
            inOrder.verify(pokemonCache).getPokemons();
        }

        @Test
        void testShouldReturnEmptyResultWhenNoMatchFound() {
            when(pokemonCache.isValid()).thenReturn(true);
            when(pokemonCache.getPokemons()).thenReturn(List.of("pikachu"));

            PokemonListResponse result = pokemonService.searchPokemons("xyz", SortType.ALPHABETICAL);

            assertThat(result.getResult()).isEmpty();
            inOrder.verify(pokemonCache).isValid();
            inOrder.verify(pokemonCache).getPokemons();
        }

        @Test
        void testShouldUseCacheWhenValid() {
            when(pokemonCache.isValid()).thenReturn(true);
            when(pokemonCache.getPokemons()).thenReturn(List.of("pikachu"));

            pokemonService.searchPokemons(null, SortType.ALPHABETICAL);

            inOrder.verify(pokemonCache).isValid();
            inOrder.verify(pokemonCache).getPokemons();
        }

        @Test
        void testShouldFetchFromApiAndPopulateCacheWhenCacheIsInvalid() {
            List<String> apiPokemons = List.of("bulbasaur", "pikachu");
            when(pokemonCache.isValid()).thenReturn(false);
            when(pokeApiClient.fetchAllPokemonNames()).thenReturn(apiPokemons);
            doNothing().when(pokemonCache).setPokemons(apiPokemons);

            PokemonListResponse result = pokemonService.searchPokemons(null, SortType.ALPHABETICAL);

            assertThat(result.getResult()).containsExactly("bulbasaur", "pikachu");
            inOrder.verify(pokemonCache).isValid();
            inOrder.verify(pokeApiClient).fetchAllPokemonNames();
            inOrder.verify(pokemonCache).setPokemons(apiPokemons);
        }
    }

    @Nested
    class SearchPokemonsWithHighlightTest {

        @Test
        void testShouldReturnHighlightWithPreTagsAroundMatch() {
            when(pokemonCache.isValid()).thenReturn(true);
            when(pokemonCache.getPokemons()).thenReturn(List.of("pikachu"));

            PokemonHighlightResponse result = pokemonService.searchPokemonsWithHighlight("ika", SortType.ALPHABETICAL);

            assertThat(result.getResult()).hasSize(1);
            assertThat(result.getResult().get(0).getName()).isEqualTo("pikachu");
            assertThat(result.getResult().get(0).getHighlight()).isEqualTo("p<pre>ika</pre>chu");
            inOrder.verify(pokemonCache).isValid();
            inOrder.verify(pokemonCache).getPokemons();
        }

        @Test
        void testShouldPreserveOriginalCasingInHighlight() {
            when(pokemonCache.isValid()).thenReturn(true);
            when(pokemonCache.getPokemons()).thenReturn(List.of("Pikachu"));

            PokemonHighlightResponse result = pokemonService.searchPokemonsWithHighlight("pi", SortType.ALPHABETICAL);

            assertThat(result.getResult()).hasSize(1);
            assertThat(result.getResult().get(0).getName()).isEqualTo("Pikachu");
            assertThat(result.getResult().get(0).getHighlight()).isEqualTo("<pre>Pi</pre>kachu");
            inOrder.verify(pokemonCache).isValid();
            inOrder.verify(pokemonCache).getPokemons();
        }

        @Test
        void testShouldReturnFullNameAsHighlightWhenQueryIsEmpty() {
            when(pokemonCache.isValid()).thenReturn(true);
            when(pokemonCache.getPokemons()).thenReturn(List.of("pikachu"));

            PokemonHighlightResponse result = pokemonService.searchPokemonsWithHighlight("", SortType.ALPHABETICAL);

            assertThat(result.getResult()).hasSize(1);
            assertThat(result.getResult().get(0).getName()).isEqualTo("pikachu");
            assertThat(result.getResult().get(0).getHighlight()).isEqualTo("pikachu");
            inOrder.verify(pokemonCache).isValid();
            inOrder.verify(pokemonCache).getPokemons();
        }
    }
}
