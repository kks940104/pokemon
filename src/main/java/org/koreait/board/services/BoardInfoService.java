package org.koreait.board.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.board.controllers.BoardSearch;
import org.koreait.board.entities.Board;
import org.koreait.board.entities.BoardData;
import org.koreait.board.entities.QBoardData;
import org.koreait.board.exceptions.BoardDataNotFoundException;
import org.koreait.board.repositories.BoardDataRepository;
import org.koreait.board.services.configs.BoardConfigInfoService;
import org.koreait.global.libs.Utils;
import org.koreait.global.paging.ListData;
import org.koreait.global.paging.Pagination;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardInfoService {


    private final BoardConfigInfoService configInfoService;
    private final BoardDataRepository boardDataRepository;
    private final JPAQueryFactory queryFactory;
    private final HttpServletRequest request;
    private final Utils utils;

    /**
     * 게시글 1개 조회
     * @param seq
     * @return
     */
    public BoardData get(Long seq) {

        BoardData item = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);

        addInfo(item); // 추가 정보 처리

        return null;
    }

    /**
     * 게시글 목록
     * @param search
     * @return
     */
    public ListData<BoardData> getList(BoardSearch search) {
        int page = Math.max(search.getPage(), 1);
        Board board = null;
        int rowsPerPage = 0;
        List<String> bids = search.getBid();
        if (bids != null && bids.isEmpty()) {
            board = configInfoService.get(bids.get(0));
            rowsPerPage = board.getRowsPerPage();
        }
        int limit = search.getLimit() > 0 ? search.getLimit() : rowsPerPage;
        int offset = (page - 1) * limit; // 페이지의 시작 위치.

        // region 검색처리

        BooleanBuilder andBuilder = new BooleanBuilder();
        QBoardData boardData = QBoardData.boardData;

        // 게시판 아이디
        if (bids != null && !bids.isEmpty()) {
            andBuilder.and(boardData.board.bid.in(bids)); // eq랑 in 조건 어떻게 되는지 확인해보자.
        }

        /**
         * 키워드 검색
         * sopt
         *      - ALL - 제목 + 내용 + 작성자(작성자 + 이메일 + 회원명)
         *      - SUBJECT - 제목
         *      - CONTENT - 내용
         *      - SUBJECT_CONTENT - 제목 + 내용
         *      - POSTER - 작성자 + 이메일 + 회원명
         */

        String sopt = search.getSopt();
        String skey = search.getSkey();
        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";

        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            StringExpression subject = boardData.subject;
            StringExpression content = boardData.content;
            StringExpression poster = boardData.poster.concat(boardData.member.name)
                    .concat(boardData.member.email);
            StringExpression condition = null;
            if (sopt.equals("SUBJECT")) { // 제목 검색
                condition = subject;
            } else if (sopt.equals("content")) { // 내용 검색
                condition = content;
            } else if (sopt.equals("SUBJECT_CONTENT")) { // 제목 + 내용
                condition = subject.concat(content);
            } else if (sopt.equals("POSTER")) { // 작성자 검색
                condition = poster;
            } else { // 통합 검색
                condition = subject.concat(content).concat(poster);
            }
            andBuilder.and(condition.contains(skey));
        }

        // endregion

        List<BoardData> items =
                queryFactory.selectFrom(boardData)
                        .leftJoin(boardData.board) // 지연로딩
                        .fetchJoin() // 이므로 빨리 가져오려고
                        .leftJoin(boardData.member)
                        .fetchJoin()
                        .where(andBuilder)
                        .offset(offset)
                        .limit(limit)
                        // 공지사항이 가장 위로 가져오게
                        .orderBy(boardData.notice.desc(), boardData.createdAt.desc())
                        .fetch();

        long total = boardDataRepository.count();

        items.forEach(this::addInfo); // 추가정보처리

        int ranges = utils.isMobile() ? 5 : 10;
        if (board != null) { // 게시판별 설정이 있는 경우
            ranges = utils.isMobile() ? board.getPageRangesMobile() : board.getPageRanges();
        }

        Pagination pagination = new Pagination(page, (int) total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 게시판별 조회
     * @param bid
     * @param search
     * @return
     */
    public ListData<BoardData> getList(String bid, BoardSearch search) {
        search.setBid(List.of(bid));
        return getList(search);
    }

    /**
     * 게시판별 최신 게시글
     * @param bid
     * @param limit
     * @return
     */
    public List<BoardData> getLatest(String bid, int limit) {
        BoardSearch search = new BoardSearch();
        search.setLimit(limit);
        search.setBid(List.of(bid));
        ListData<BoardData> data = getList(search);
        return data.getItems();
    }

    public List<BoardData> getLatest(String bid) {
        return getLatest(bid, 5);
    }

    /**
     * 추가 정보 처리
     * @param item
     */
    private void addInfo(BoardData item) {

    }

}
