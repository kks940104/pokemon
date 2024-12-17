package org.koreait.email.services;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.koreait.email.controllers.RequestEmail;
import org.koreait.global.libs.Utils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Profile("email")
@RequiredArgsConstructor
public class EmailAuthService {

    private final Utils utils;
    private final EmailService emailService;
    private final HttpSession session;

    /**
     *
     * @param to : 수신 이메일 주소.
     * @return
     */
    public boolean sendCode(String to) {
        Random random = new Random();
        String subject = utils.getMessage("Email.authCode.subject");
        RequestEmail form = new RequestEmail();

        /**
         * 인증 코드는 5자리 정수 형태.
         * 만료시간은 3분
         * 사용자의 입력을 검즘하기 위해서 세션에 인증코드와 만료시간을 기록.
         */
        int autoCode = random.nextInt(99999);
        long expired = Instant.EPOCH.getEpochSecond() + 60 * 3; // 1970년 1월 1일부터 1초단위로 기록된 값.

        session.setAttribute("authCode", autoCode);
        session.setAttribute("expiredTime", expired);

        Map<String, Object> tplData = new HashMap<>();
        tplData.put("authCode", autoCode);

        form.setTo(List.of(to));
        form.setSubject(subject);

        return emailService.sendEmail(form, "auth", tplData);
    }
}














