package org.koreait.board.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.koreait.file.entites.FileInfo;

import java.util.List;

@Data
public class RequestBoard {
    private Long seq; // 게시글번호
    private String mode;

    @NotBlank
    private String bid; // 게시판 아이디

    @NotBlank
    private String gid;

    @NotBlank
    private String poster; // 작성자

    @Size(min=4)
    private String guestPw; // 비회원 비밀번호

    @NotBlank
    private String subject; // 글 제목

    @NotBlank
    private String content; // 글 내용
    private boolean notice; // 공지글 여부

    private List<FileInfo> editorImages; // 사진
    private List<FileInfo> attachFiles; // 첨부파일
}
