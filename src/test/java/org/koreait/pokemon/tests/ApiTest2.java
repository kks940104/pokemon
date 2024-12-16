package org.koreait.pokemon.tests;

import org.junit.jupiter.api.Test;
import org.koreait.pokemon.api.service.ApiUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApiTest2 {

    @Autowired
    private ApiUpdateService service;

    @Test
    void updateTest1() {
        //service.update(1);

        for (int i = 1; i <= 9; i++) {
            service.update(i);
        }
    }

    @Test
    void updateTest2() {
        service.updateTest(1);
    }
}