package org.koreait.games.controllers;

import lombok.Data;

import java.util.List;

@Data
public class RequestShadowGame {

    private List<Long> pokemonCount;

    private int pokemonCheck;
    private boolean row;
    private boolean mid;
    private boolean high;

    private String pokemonName;
}
