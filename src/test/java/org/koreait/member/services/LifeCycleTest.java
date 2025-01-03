package org.koreait.member.services;

import org.junit.jupiter.api.*;

public class LifeCycleTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("BEFORE ALL");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("AFTER ALL");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("BEFORE EACH");
    }

    @AfterEach
    void afterEach() {
        System.out.println("AFTER EACH");
    }

    @Test
    void test1() {
        System.out.println("TEST1");
    }

    @Test
    @Disabled // 단일테스트로만 진행.
    void test2() {
        System.out.println("TEST2");
    }

    @Test
    @Timeout(1L) // 기본값 초 단위. 나노단위까지 줄일 수 있음.
    void test3() throws InterruptedException {
        System.out.println("TEST3");
        Thread.sleep(3000);
    }
}
