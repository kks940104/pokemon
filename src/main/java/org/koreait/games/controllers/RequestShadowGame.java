package org.koreait.games.controllers;

import lombok.Data;

import java.util.List;

@Data
public class RequestShadowGame {

    private List<Long> pokemonCount;

    private boolean[] pokemonCheck = new boolean[8];
}
