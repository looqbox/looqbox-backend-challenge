package com.looqbox.challenge.controller;

import com.looqbox.challenge.dto.PokemonHighlightResponse;
import com.looqbox.challenge.dto.PokemonListResponse;
import com.looqbox.challenge.dto.SortType;
import com.looqbox.challenge.service.PokemonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    /**
     * GET /pokemons
     *
     * @param query Filtro opcional para buscar nomes de Pokémon (sem distinção entre maiúsculas e minúsculas).
     * Se omitida, todos os Pokémon serão retornados.
     * 
     * @param sort Tipo de ordenação opcional ("ALPHABETICAL" ou "LENGTH").
     * Padrão para ordenação alfabética se omitido ou não reconhecido.
     * 
     * @return Lista dos nomes dos Pokémon correspondentes ao filtro.
     */
    @GetMapping
    public ResponseEntity<PokemonListResponse> getPokemons(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "sort", required = false) String sort) {
        return ResponseEntity.ok(pokemonService.searchPokemons(query, SortType.fromString(sort)));
    }

    /**
     * GET /pokemons/highlight
     *
     * @param query Filtro opcional para buscar nomes de Pokémon (sem distinção entre maiúsculas e minúsculas).
     * Se omitida, todos os Pokémon serão retornados.
     * 
     * @param sort Tipo de ordenação opcional ("ALPHABETICAL" ou "LENGTH").
     * Padrão para ordenação alfabética se omitido ou não reconhecido.
     * 
     * @return Lista dos nomes dos Pokémon correspondentes ao filtro, com destaque para o termo de busca
     * cercado por tags {@code <pre>}
     */
    @GetMapping("/highlight")
    public ResponseEntity<PokemonHighlightResponse> getPokemonsWithHighlight(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "sort", required = false) String sort) {
        return ResponseEntity.ok(pokemonService.searchPokemonsWithHighlight(query, SortType.fromString(sort)));
    }
}
