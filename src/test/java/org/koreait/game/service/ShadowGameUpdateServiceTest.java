package org.koreait.game.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.koreait.games.controllers.RequestShadowGame;
import org.koreait.games.services.ShadowGameRankUpdateService;
import org.koreait.member.constants.Gender;
import org.koreait.member.controllers.RequestJoin;
import org.koreait.member.services.MemberUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@SpringBootTest
public class ShadowGameUpdateServiceTest {

    @Autowired
    private ShadowGameRankUpdateService shadowGameRankUpdateService;

    @Autowired
    private MemberUpdateService memberUpdateService;

    @BeforeEach
    void init() {
        RequestJoin form = new RequestJoin();
        form.setEmail("user01@test.org");
        form.setName("이이름");
        form.setNickName("닉네임");
        form.setZipCode("0000");
        form.setAddress("주소");
        form.setAddressSub("나머지 주소");
        form.setGender(Gender.MALE);
        form.setBirthDt(LocalDate.now());
        form.setPassword("_aA123456");
        form.setConfirmPassword(form.getPassword());
        memberUpdateService.process(form);
    }

    @Test
    @WithUserDetails(value="user01@test.org", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void listTest() {
        RequestShadowGame shadowGame = new RequestShadowGame();
        shadowGame.setRow(true);
        shadowGame.setGameWrongAnswer(10);
        shadowGame.setGameCorrectAnswer(10);

        shadowGameRankUpdateService.process(shadowGame);
    }
}
