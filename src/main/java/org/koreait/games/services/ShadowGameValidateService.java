package org.koreait.games.services;


import lombok.RequiredArgsConstructor;
import org.koreait.games.controllers.RequestShadowGame;
import org.koreait.pokemon.entities.Pokemon;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class ShadowGameValidateService {

    public void validatePokemon (RequestShadowGame form) {
        form.setGameQuizAnswer(false);
        if (form.getGameCount() <= 21) {
            form.setGameCount(form.getGameCount() + 1);
        }
        Pokemon pokemon = form.getGamePokemon();
        if (pokemon.getName().equals(form.getPokemonName())) {
            form.setGameQuizAnswer(true);
            form.setGameCorrectAnswer(form.getGameCorrectAnswer() + 1);
        } else {
            form.setGameWrongAnswer(form.getGameWrongAnswer() + 1);
        }
        form.setPokemonName(null);
    }
}
