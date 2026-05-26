package com.looqbox.challenge.dto;

import java.util.List;

public class PokemonHighlightResponse {

    private final List<PokemonHighlightItem> result;

    public PokemonHighlightResponse(List<PokemonHighlightItem> result) {
        this.result = result;
    }

    public List<PokemonHighlightItem> getResult() {
        return result;
    }
}
