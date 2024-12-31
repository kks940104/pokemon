package org.koreait.games.validators;

import lombok.AllArgsConstructor;
import org.koreait.games.controllers.RequestShadowGame;
import org.koreait.pokemon.entities.Pokemon;
import org.koreait.pokemon.services.PokemonInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
@AllArgsConstructor
public class ShadowGameValidators implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestShadowGame.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestShadowGame request = (RequestShadowGame) target;
        if(request.getGameCount() >= 21) {
            errors.rejectValue("gameCount", "error.shadowGame");
        }
    }
}
