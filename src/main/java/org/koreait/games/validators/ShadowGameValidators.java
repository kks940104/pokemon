package org.koreait.games.validators;

import org.koreait.games.controllers.RequestShadowGame;
import org.koreait.games.exceptions.SelectCheckException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
public class ShadowGameValidators implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestShadowGame.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestShadowGame request = (RequestShadowGame) target;
        boolean[] valid = request.getPokemonCheck();
        for (int i = 0; i < valid.length; i++) {
            if (valid[i]) {
                return;
            }
        }
        throw new SelectCheckException();
    }
}
