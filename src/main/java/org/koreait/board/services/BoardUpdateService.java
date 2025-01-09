package org.koreait.board.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.koreait.board.services.configs.BoardConfigInfoService;
import org.springframework.transaction.annotation.Transactional;
import org.koreait.board.exceptions.BoardDataNotFoundException;
import org.koreait.board.repositories.BoardDataRepository;
import org.springframework.context.annotation.Lazy;
import org.koreait.board.controllers.RequestBoard;
import org.koreait.file.services.FileDoneService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.koreait.board.entities.BoardData;
import org.springframework.util.StringUtils;
import org.koreait.member.libs.MemberUtil;
import org.koreait.board.entities.Board;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Lazy
@Service
@Transactional // 이거 어디껀지 봐야겠다
@RequiredArgsConstructor
public class BoardUpdateService {

    private final BoardConfigInfoService configInfoService; // 게시판 설정 가져오기
    private final BoardDataRepository boardDataRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileDoneService fileDoneService;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;

    public BoardData process(RequestBoard form) {

        Long seq = Objects.requireNonNullElse(form.getSeq(), 0L);
        String mode = Objects.requireNonNullElse(form.getMode(), "write");

        BoardData data = null;
        if (mode.equals("edit")) { // 수정 시
            data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);
        } else { // 추가 시
            /**
             * 등록될때만 최초 한번 기록되는 데이터
             * - 게시판 설정, 회원
             * - gid
             * - 아이피, UserAgent
             */
            Board board = configInfoService.get(form.getBid());
            data = new BoardData();
            data.setBoard(board);
            data.setMember(memberUtil.getMember());
            data.setGid(form.getGid());
            data.setIpAddr(request.getRemoteAddr()); // IP 주소...
            data.setUserAgent(request.getHeader("User-Agent"));
        }

        // 글등록, 글 수정 공통 반영 사항
        String guestPw = form.getGuestPw();
        if (StringUtils.hasText(guestPw)) { // 비회원 비밀번호가 있을 경우...
            data.setGuestPw(passwordEncoder.encode(guestPw));
        }

        data.setPoster(form.getPoster());

        if (memberUtil.isAdmin()) { // 공지글 여부는 관리자만 반영 가능.
            data.setNotice(form.isNotice());
        }

        data.setSubject(form.getSubject());
        data.setContent(form.getContent());
        data.setExternalLink(form.getExternalLink());
        data.setYoutubeUrl(form.getYoutubeUrl());
        data.setCategory(form.getCategory());

        boardDataRepository.saveAndFlush(data);

        fileDoneService.process(form.getGid());

        return data;
    }
}














