package org.koreait.member.validators;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.koreait.global.validator.PasswordValidator;
import org.koreait.member.controllers.RequestAgree;
import org.koreait.member.controllers.RequestJoin;
import org.koreait.member.reporitories.MemberRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.Period;

@Lazy
@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator, PasswordValidator {

    private final MemberRepository memberRepository;
    private final HttpSession session;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestAgree.class)
            || clazz.isAssignableFrom(RequestJoin.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        // 커맨드 객체 검증 실패시에는 추가 검증은 진행 X
        // MemberController.joinPS(@Valid RequestJoin form)
        if (errors.hasErrors()) {
            return;
        }

        if (target instanceof RequestJoin requestJoin) {
            validateJoin(requestJoin, errors);
        } else {
            validateAgree((RequestAgree) target, errors);
        }
    }

    /**
     * 약관 동의 검증
     * @param form
     * @param errors
     */

    private void validateAgree(RequestAgree form, Errors errors) {
        if (!form.isRequiredTerms1()) {
            errors.rejectValue("requiredTerms1", "AssertTrue");
        }
        if (!form.isRequiredTerms2()) {
            errors.rejectValue("requiredTerms2", "AssertTrue");
        }
        if (!form.isRequiredTerms3()) {
            errors.rejectValue("requiredTerms3", "AssertTrue");
        }
    }

    /**
     * 회원 가입 검증
     * @param form
     * @param errors
     */
    private void validateJoin(RequestJoin form, Errors errors) {
        /**
         * 1. 이메일 중복 여부 체크
         * 2. 비밀번호 복잡성 - 알파벳 대소문자 각각 1개 이상, 숫자 1개 이상, 특수 문자 포함
         * 3. 비밀번호, 비밀번호 확인 일치 여부
         * 4. 생년월일을 입력받으면 14세 이상만 가능하게 통제
         * 5. 이메일 인증 완료 여부 체크
         */


        System.out.println("테스트");
        String email = form.getEmail();
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        LocalDate birthDt = form.getBirthDt();


        // region 1. 이메일 중복 여부 체크

        if(memberRepository.exists(email)) {
            errors.rejectValue("email", "Duplicated");
        }

        // endregion

        // region 2. 비밀번호 복잡성

        if (!alphaCheck(password, false) || !numberCheck(password) || !specialCharsCheck(password)) {
            errors.rejectValue("password", "Complexity");
        }

        // endregion

        // region 3. 비밀번호, 비밀번호 확인 일치 여부

        if (!password.equals(confirmPassword)) {
            errors.rejectValue("confirmPassword","Mismatch");
        }

        // endregion

        // region 4. 생년월일을 입력받으면 만 14세 이상만 가입 가능하게 통제

        Period period = Period.between(birthDt, LocalDate.now());
        int year = period.getYears();
        if (year < 14) { // 만 14세 미만인 경우
            errors.rejectValue("birthDt", "UnderAge");
        }

        // endregion

        // region 5. 이메일 인증 완료 여부 체크

        Boolean authCodeVerified = (Boolean) session.getAttribute("authCodeVerified");
        if (authCodeVerified == null || !authCodeVerified) {
            errors.reject("NotVerified.authCode");
        }

        // endregion

    }
}














