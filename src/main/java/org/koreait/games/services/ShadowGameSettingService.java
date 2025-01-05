package org.koreait.games.services;

import lombok.RequiredArgsConstructor;
import org.koreait.games.controllers.RequestShadowGame;
import org.koreait.global.libs.Utils;
import org.koreait.pokemon.entities.Pokemon;
import org.koreait.pokemon.services.PokemonInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.LongStream;

@Lazy
@Service
@RequiredArgsConstructor
public class ShadowGameSettingService {

    public List<Long> pokemonGameSetting(RequestShadowGame form) {
        List<Long> pokemonCounts = new ArrayList<>();
        form.setGameCorrectAnswer(0);
        form.setGameWrongAnswer(0);
        form.setGameCount(1);
        Collections.addAll(pokemonCounts,LongStream.range(1L, form.getPokemonCheck() + 1L).boxed().toArray(Long[]::new));
        levelSetting(form, form.getPokemonCheck());

        return pokemonCounts;
    }

    private void levelSetting(RequestShadowGame form, Long count) {
        if (count == 386) {
            form.setRow(true);
            form.setMid(false);
            form.setHigh(false);
        } else if (count == 649) {
            form.setRow(false);
            form.setMid(true);
            form.setHigh(false);
        } else if (count == 898) {
            form.setRow(false);
            form.setMid(false);
            form.setHigh(false);
        }
    }
}

































