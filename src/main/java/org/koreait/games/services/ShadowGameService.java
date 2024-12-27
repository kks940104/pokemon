package org.koreait.games.services;

import lombok.RequiredArgsConstructor;
import org.koreait.games.controllers.RequestShadowGame;
import org.koreait.global.libs.Utils;
import org.koreait.pokemon.entities.Pokemon;
import org.koreait.pokemon.services.PokemonInfoService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class ShadowGameService {

    private final PokemonInfoService pokemonInfoService;
    private final Utils utils;

    public List<Long> pokemonCount(RequestShadowGame form) {
        List<Long> pokemonCounts = new ArrayList<>();

        if(form.getPokemonCheck() == 386) {
            Collections.addAll(pokemonCounts,LongStream.range(1L, 387L).boxed().toArray(Long[]::new));
            levelSetting(form, 386);
        } else if (form.getPokemonCheck() == 649) {
            Collections.addAll(pokemonCounts,LongStream.range(1L, 650L).boxed().toArray(Long[]::new));
            levelSetting(form, 649);
        } else if (form.getPokemonCheck() == 898) {
            levelSetting(form, 898);
            Collections.addAll(pokemonCounts,LongStream.range(1L, 899L).boxed().toArray(Long[]::new));
        }

        return pokemonCounts;
    }

    private void levelSetting(RequestShadowGame form, int count) {
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

    public Pokemon findPokemon(RequestShadowGame form) {
        Random random = new Random();
        int checkQuiz = random.nextInt(form.getPokemonCount().size());
        return pokemonInfoService.get(form.getPokemonCount().remove(checkQuiz));
    }

    public String getFlavors(Pokemon pokemon) {
        List<String> flavors = new ArrayList<>(pokemon.get_flavorText().values());

        return flavors == null ? "" : utils.nl2br(flavors.get(0));
    }
}
