package org.koreait.admin.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.koreait.member.constants.Authority;

import java.util.List;

@Data
public class RequestBoard {

    private String mode;

    @NotBlank
    private String bid; // 게시판 아이디

    @NotBlank
    private String name; // 게시판 명
    private boolean open;
    private String category; // 줄개행 문자로 여러 분류를 등록.
    private int rowsPerPage; // 1페이지당 게시글 갯수
    private int pageRanges; // Front 뷰일때 노출되는 페이지 링크 갯수
    private int pageRangesMobile; // Mobile 뷰일때 노출되는 페이지 링크 갯수
    private boolean useEditor; // 에디터 사용 여부
    private boolean useEditorImage; // 에디터 첨부 이미지 노출 사용 여부
    private boolean useAttachFile; // 다운로드용 첨부 파일 사용 여부
    private boolean useComment; // 댓글 사용 여부
    private String locationAfterWriting; // 글 작성 후 이동 경로 list, view
    private String skin; // 게시판 스킨. 기본값 - default

    private Authority listAuthority; // 목록 접근 권한
    private Authority viewAuthority; // 글보기 접근 권한
    private Authority writeAuthority; // 글쓰기, 수정, 삭제 권한
    private Authority commentAuthority; // 댓글 작성 권한
}
