package org.koreait.member.social.entites;

import lombok.Data;

@Data
public class SocialConfig {
    private boolean useKakaoLogin; // 카카오 로그인 사용 여부
    private String kakaoRestApiKey;
}
