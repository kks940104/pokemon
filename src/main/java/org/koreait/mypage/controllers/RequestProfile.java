package org.koreait.mypage.controllers;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.koreait.file.entites.FileInfo;
import org.koreait.member.constants.Authority;
import org.koreait.member.constants.Gender;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class RequestProfile {

    private String mode;

    private String email; // 관리자일때만....

    @NotBlank
    private String name; // 회원명
    @NotBlank
    private String nickName; // 닉네임

    private String password; // 비밀번호
    private String confirmPassword; // 비밀번호확인.
    @NotNull
    private Gender gender; // 성별

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDt; // 생년월일
    @NotBlank
    private String zipCode;
    @NotBlank
    private String address;
    private String addressSub;

    private List<String> optionalTerms; // 추가 선택 약관

    private List<Authority> authorities;

    private FileInfo profileImage;
}
