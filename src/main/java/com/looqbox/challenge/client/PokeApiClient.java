package com.looqbox.challenge.client;

import com.looqbox.challenge.client.dto.PokeApiPokemon;
import com.looqbox.challenge.client.dto.PokeApiResponse;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Cliente HTTP responsável por buscar dados de Pokémon da PokéAPI.
 *
 * Gargalo: buscar todos os nomes de Pokémon em cada requisição causaria
 * alta latência. Isso é mitigado pelo PokemonCache.
 */
@Component
public class PokeApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PokeApiClient.class);

    private final RestClient restClient;

    public PokeApiClient(RestClient.Builder restClientBuilder,
                         @Value("${pokeapi.base-url}") String baseUrl) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Busca todos os nomes de Pokémon da PokéAPI em uma única requisição.
     * Usa um valor limite alto para recuperar todos os Pokémon disponíveis de uma só vez,
     * evitando a sobrecarga de paginação.
     * 
     * @return Lista de nomes de Pokémon.
     */
    public List<String> fetchAllPokemonNames() {
        LOGGER.debug("Buscando Pokemons na API Externa (PokeApi) ");
        PokeApiResponse response = restClient.get()
                .uri("/pokemon?limit=100000&offset=0")
                .retrieve()
                .body(PokeApiResponse.class);

        if (response == null || response.getResults() == null) {
            return new ArrayList<>();
        }

        List<String> names = new ArrayList<>(response.getResults().size());
        for (PokeApiPokemon pokemon : response.getResults()) {
            names.add(pokemon.getName());
        }
        return names;
    }
}
