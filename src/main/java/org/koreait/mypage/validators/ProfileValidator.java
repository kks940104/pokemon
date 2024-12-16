package org.koreait.mypage.validators;


import org.koreait.global.validator.PasswordValidator;
import org.koreait.mypage.controllers.RequestProfile;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
public class ProfileValidator implements Validator, PasswordValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestProfile.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestProfile form = (RequestProfile) target;
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();

        if (!StringUtils.hasText(password)) { // 비밀번호 있는지 확인.
            return;
        }

        if (password.length() < 8) {
            errors.rejectValue("password","Size");
        }

        if (!StringUtils.hasText(confirmPassword)) { // 비밀번호 확인이 있는지 확인.
            errors.rejectValue("confirmPassword","NotBlank");
            return;
        }

        // region 1. 비밀번호 복잡성

        if (!alphaCheck(password, false) || !numberCheck(password) || !specialCharsCheck(password)) {
            errors.rejectValue("password", "Complexity");
        }

        // endregion

        // region 2. 비밀번호, 비밀번호 확인 일치 여부

        if (!password.equals(confirmPassword)) {
            errors.rejectValue("confirmPassword","Mismatch");
        }

        // endregion
    }
}
