package org.koreait.message.validators;

import lombok.RequiredArgsConstructor;
import org.koreait.member.libs.MemberUtil;
import org.koreait.member.reporitories.MemberRepository;
import org.koreait.message.controllers.RequestMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class MessageValidator implements Validator {

    private final MemberUtil memberUtil;
    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestMessage.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestMessage form = (RequestMessage) target;
        String email = form.getEmail();

        boolean notice = form.isNotice();

        if (!memberUtil.isAdmin() && notice) { // 관리자가 아니지만 공지 쪽지이면 X
            notice = false;
            form.setNotice(notice);
        }

        if (!memberUtil.isAdmin() && !notice && !StringUtils.hasText(email)) {
            errors.rejectValue("email", "NotBlank");
        }

        if (!notice && !memberRepository.exists(email)) {
            errors.reject("NotFound.member");
        }

/*        if (!memberRepository.exists(email)) { // 수신하는 쪽 회원이 존재 X
            errors.reject("NotFound.member");
        }*/

    }
}















