package com.looqbox.challenge.controller;

import com.looqbox.challenge.dto.PokemonHighlightItem;
import com.looqbox.challenge.dto.PokemonHighlightResponse;
import com.looqbox.challenge.dto.PokemonListResponse;
import com.looqbox.challenge.dto.SortType;
import com.looqbox.challenge.service.PokemonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonControllerTest {

    @InjectMocks
    PokemonController controller;

    @Mock
    PokemonService pokemonService;

    InOrder inOrder;
    Object[] allMocks;

    @BeforeEach
    void setUp() {
        allMocks = new Object[]{pokemonService};
        inOrder = Mockito.inOrder(allMocks);
    }

    @AfterEach
    void tearDown() {
        inOrder.verifyNoMoreInteractions();
        verifyNoMoreInteractions(allMocks);
    }

    @Nested
    class GetPokemonsTest {

        @Test
        void testShouldReturnOkWithPokemonListForGetPokemons() {
            PokemonListResponse response = new PokemonListResponse(List.of("bulbasaur", "pikachu"));
            when(pokemonService.searchPokemons(null, SortType.ALPHABETICAL)).thenReturn(response);

            ResponseEntity<PokemonListResponse> result = controller.getPokemons(null, null);

            assertThat(result.getStatusCode().value()).isEqualTo(200);
            assertThat(result.getBody()).isEqualTo(response);
            inOrder.verify(pokemonService).searchPokemons(null, SortType.ALPHABETICAL);
        }

        @Test
        void testShouldPassLengthSortTypeToService() {
            PokemonListResponse response = new PokemonListResponse(List.of("mew", "pikachu"));
            when(pokemonService.searchPokemons(null, SortType.LENGTH)).thenReturn(response);

            ResponseEntity<PokemonListResponse> result = controller.getPokemons(null, "LENGTH");

            assertThat(result.getStatusCode().value()).isEqualTo(200);
            assertThat(result.getBody()).isEqualTo(response);
            inOrder.verify(pokemonService).searchPokemons(null, SortType.LENGTH);
        }

        @Test
        void testShouldDefaultToAlphabeticalWhenSortParamIsAbsent() {
            PokemonListResponse response = new PokemonListResponse(List.of("pikachu"));
            when(pokemonService.searchPokemons("pika", SortType.ALPHABETICAL)).thenReturn(response);

            ResponseEntity<PokemonListResponse> result = controller.getPokemons("pika", null);

            assertThat(result.getStatusCode().value()).isEqualTo(200);
            assertThat(result.getBody()).isEqualTo(response);
            inOrder.verify(pokemonService).searchPokemons("pika", SortType.ALPHABETICAL);
        }
    }

    @Nested
    class GetPokemonsWithHighlightTest {

        @Test
        void testShouldReturnOkWithHighlightResponseForGetPokemonsHighlight() {
            PokemonHighlightResponse response = new PokemonHighlightResponse(
                    List.of(new PokemonHighlightItem("pikachu", "pikachu")));
            when(pokemonService.searchPokemonsWithHighlight(null, SortType.ALPHABETICAL)).thenReturn(response);

            ResponseEntity<PokemonHighlightResponse> result = controller.getPokemonsWithHighlight(null, null);

            assertThat(result.getStatusCode().value()).isEqualTo(200);
            assertThat(result.getBody()).isEqualTo(response);
            inOrder.verify(pokemonService).searchPokemonsWithHighlight(null, SortType.ALPHABETICAL);
        }

        @Test
        void testShouldPassLengthSortTypeToServiceForHighlightEndpoint() {
            PokemonHighlightResponse response = new PokemonHighlightResponse(
                    List.of(new PokemonHighlightItem("mew", "mew")));
            when(pokemonService.searchPokemonsWithHighlight(null, SortType.LENGTH)).thenReturn(response);

            ResponseEntity<PokemonHighlightResponse> result = controller.getPokemonsWithHighlight(null, "LENGTH");

            assertThat(result.getStatusCode().value()).isEqualTo(200);
            assertThat(result.getBody()).isEqualTo(response);
            inOrder.verify(pokemonService).searchPokemonsWithHighlight(null, SortType.LENGTH);
        }
    }
}
