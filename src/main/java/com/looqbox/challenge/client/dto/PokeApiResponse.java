package com.looqbox.challenge.client.dto;

import java.util.List;

public class PokeApiResponse {

    private int count;
    private List<PokeApiPokemon> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PokeApiPokemon> getResults() {
        return results;
    }

    public void setResults(List<PokeApiPokemon> results) {
        this.results = results;
    }
}
