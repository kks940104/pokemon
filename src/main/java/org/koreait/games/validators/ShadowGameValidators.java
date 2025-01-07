package org.koreait.games.validators;

import lombok.AllArgsConstructor;
import org.koreait.games.controllers.RequestShadowGame;
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
        System.out.println("체크용" + request.getGameCount());
        if(request.getGameCount() >= 21) {
            errors.rejectValue("gameCount", "error.shadowGame");
        }
    }
}
