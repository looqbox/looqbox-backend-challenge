package com.looqbox.challenge.dto;

import java.util.List;

public class PokemonListResponse {

    private final List<String> result;

    public PokemonListResponse(List<String> result) {
        this.result = result;
    }

    public List<String> getResult() {
        return result;
    }
}
