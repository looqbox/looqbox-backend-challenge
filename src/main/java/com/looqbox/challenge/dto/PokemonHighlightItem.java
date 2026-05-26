package com.looqbox.challenge.dto;

public class PokemonHighlightItem {

    private final String name;
    private final String highlight;

    public PokemonHighlightItem(String name, String highlight) {
        this.name = name;
        this.highlight = highlight;
    }

    public String getName() {
        return name;
    }

    public String getHighlight() {
        return highlight;
    }
}
