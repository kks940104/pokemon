package org.koreait.pokemon.services;

import org.junit.jupiter.api.Test;
import org.koreait.games.services.ShadowGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PokemonGameService {

    @Autowired
    private ShadowGameService shadowGameService;

    @Test
    void test1() {
        // System.out.println(shadowGameService.pokemonCount());
    }
}
