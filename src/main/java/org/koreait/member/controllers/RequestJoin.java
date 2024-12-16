package org.koreait.member.controllers;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.koreait.member.constants.Gender;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RequestJoin extends RequestAgree {

    @Email
    @NotBlank
    private String email; // 이메일

    @NotBlank
    private String name; // 회원명

    @NotBlank
    @Size(min=8)
    private String password; // 비밀번호

    @NotBlank
    private String confirmPassword; // 비밀번호 확인

    @NotBlank
    private String nickName; // 닉네임

    @NotNull
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-DD")
    private LocalDate birthDt;  // 생년월일

    @NotNull
    private Gender gender; // 성별

    @NotBlank
    private String zipCode; // 우편번호

    @NotBlank
    private String address; // 주소
    private String addressSub; // 나머지 주소
}