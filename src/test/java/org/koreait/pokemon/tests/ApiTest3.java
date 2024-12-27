package org.koreait.pokemon.tests;

import org.junit.jupiter.api.Test;
import org.koreait.global.libs.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"default", "test"})
public class ApiTest3 {
    @Autowired
    private Utils utils;

    @Test
    void test1() {
        String text = "\"우수한 트레이너의 명령에는\n절대복종한다. 먼 옛날\n무리를 지어 행동했던 영향이다.\"";
        System.out.println(utils.nl2br(text));

    }
}
