package com.looqbox.challenge.client;

import com.looqbox.challenge.client.dto.PokeApiPokemon;
import com.looqbox.challenge.client.dto.PokeApiResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokeApiClientTest {

    PokeApiClient pokeApiClient;

    @Mock
    RestClient.Builder restClientBuilder;

    @Mock
    RestClient restClient;

    @Mock
    RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    RestClient.ResponseSpec responseSpec;

    InOrder inOrder;
    Object[] allMocks;

    @BeforeEach
    void setUp() {
        final String baseUrl = "https://pokeapi.co/api/v2";

        when(restClientBuilder.baseUrl(baseUrl)).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);

        pokeApiClient = new PokeApiClient(restClientBuilder, baseUrl);

        allMocks = new Object[]{restClient, requestHeadersUriSpec, responseSpec};
        inOrder = Mockito.inOrder(allMocks);
    }

    @AfterEach
    void tearDown() {
        inOrder.verifyNoMoreInteractions();
        verifyNoMoreInteractions(allMocks);
    }

    @Nested
    class FetchAllPokemonNamesTest {

        @Test
        void testShouldReturnPokemonNamesFromResponse() {
            PokeApiResponse response = buildResponse("bulbasaur", "pikachu");
            configureRestClient(response);

            List<String> result = pokeApiClient.fetchAllPokemonNames();

            assertThat(result).containsExactly("bulbasaur", "pikachu");
            inOrder.verify(restClient).get();
            inOrder.verify(requestHeadersUriSpec).uri("/pokemon?limit=100000&offset=0");
            inOrder.verify(requestHeadersUriSpec).retrieve();
            inOrder.verify(responseSpec).body(PokeApiResponse.class);
        }

        @Test
        void testShouldReturnEmptyListWhenResponseIsNull() {
            configureRestClient(null);

            List<String> result = pokeApiClient.fetchAllPokemonNames();

            assertThat(result).isEmpty();
            inOrder.verify(restClient).get();
            inOrder.verify(requestHeadersUriSpec).uri("/pokemon?limit=100000&offset=0");
            inOrder.verify(requestHeadersUriSpec).retrieve();
            inOrder.verify(responseSpec).body(PokeApiResponse.class);
        }

        @Test
        void testShouldReturnEmptyListWhenResultsIsNull() {
            PokeApiResponse response = new PokeApiResponse();
            response.setResults(null);
            configureRestClient(response);

            List<String> result = pokeApiClient.fetchAllPokemonNames();

            assertThat(result).isEmpty();
            inOrder.verify(restClient).get();
            inOrder.verify(requestHeadersUriSpec).uri("/pokemon?limit=100000&offset=0");
            inOrder.verify(requestHeadersUriSpec).retrieve();
            inOrder.verify(responseSpec).body(PokeApiResponse.class);
        }

        private void configureRestClient(PokeApiResponse response) {
            doReturn(requestHeadersUriSpec).when(restClient).get();
            doReturn(requestHeadersUriSpec).when(requestHeadersUriSpec).uri("/pokemon?limit=100000&offset=0");
            when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(PokeApiResponse.class)).thenReturn(response);
        }
    }

    private PokeApiResponse buildResponse(String... names) {
        List<PokeApiPokemon> pokemons = new ArrayList<>();
        for (String name : names) {
            PokeApiPokemon pokemon = new PokeApiPokemon();
            pokemon.setName(name);
            pokemons.add(pokemon);
        }
        PokeApiResponse response = new PokeApiResponse();
        response.setResults(pokemons);
        return response;
    }
}
