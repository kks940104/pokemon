package org.koreait.games.services;

import lombok.RequiredArgsConstructor;
import org.koreait.games.controllers.RequestShadowGame;
import org.koreait.games.entitis.GameRank;
import org.koreait.games.entitis.GameRankId;
import org.koreait.games.repositories.ShadowGameRepository;
import org.koreait.global.libs.Utils;
import org.koreait.pokemon.entities.Pokemon;
import org.koreait.pokemon.services.PokemonInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Lazy
@Service
@RequiredArgsConstructor
public class ShadowGameInfoService {


    private final Utils utils;
    private final PokemonInfoService pokemonInfoService;
    private final ShadowGameRepository shadowGameRepository;

    public Pokemon findPokemon(RequestShadowGame form) {
        Random random = new Random();
        int checkQuiz = random.nextInt(form.getPokemonCount().size());
        Pokemon pokemon = pokemonInfoService.get(form.getPokemonCount().remove(checkQuiz));
        form.setGamePokemon(pokemon);
        return pokemon;
    }

    public String getFlavors(Pokemon pokemon) {
        List<String> flavors = new ArrayList<>(pokemon.get_flavorText().values());

        return flavors == null ? "" : utils.nl2br(flavors.get(0));
    }
/*
    public GameRank get() {
        GameRankId gameRankId = new GameRankId();
        GameRank rank = shadowGameRepository.getReferenceById(gameRankId);
    }*/
}
