package org.koreait.games.services;


import java.util.List;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import com.querydsl.core.BooleanBuilder;
import org.koreait.global.paging.ListData;
import org.koreait.games.entitis.GameRank;
import org.koreait.games.entitis.QGameRank;
import org.springframework.util.StringUtils;
import org.koreait.global.paging.Pagination;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import org.koreait.games.controllers.RankSearch;
import com.querydsl.core.types.dsl.DateTimePath;
import org.springframework.context.annotation.Lazy;
import com.querydsl.core.types.dsl.StringExpression;
import org.koreait.games.repositories.ShadowGameRepository;



@Lazy
@Service
@RequiredArgsConstructor
public class ShadowGameRankInfoService {

    private final HttpServletRequest request;
    private final JPAQueryFactory queryFactory;
    private final ShadowGameRepository repository;

    public ListData<GameRank> getList(RankSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        int offset = (page - 1) * limit;
        QGameRank rank = QGameRank.gameRank;

        // region 검색 처리

        BooleanBuilder andBuilder = new BooleanBuilder();

        // region 키워드 검색

        String sopt = search.getSopt(); // 검색 옵션
        String skey = search.getSkey(); // 검색 키워드
        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";

        /**
         * sopt - ALL : 통합 검색 - 이메일 + 회원명 + 닉네임
         *       NAME : 회원명 + 닉네임
         *      EMAIL : 이메일
         */
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();
            StringExpression condition;
            if (sopt.equals("EMAIL")) {
                condition = rank.member.email;
            } else if (sopt.equals("NAME")) {
                condition = rank.member.email.concat(rank.member.nickName);
            } else {
                condition = rank.member.email.concat(rank.member.name).concat(rank.member.nickName);
            }

            andBuilder.and(condition.contains(skey));
        }

        // endregion

        // region 이메일 검색

        List<String> emails = search.getEmail();

        if (emails != null && !emails.isEmpty()) {
            andBuilder.and(rank.member.email.in(emails));
        }

        // endregion

        // region 날짜 검색

        String dateType = search.getDateType();
        dateType = StringUtils.hasText(dateType) ? dateType : "createdAt"; // 가입일 기준
        LocalDate sDate = search.getSDate();
        LocalDate eDate = search.getEDate();

        DateTimePath<LocalDateTime> condition;
        if (dateType.equals("deletedAt")) condition = rank.member.deletedAt; // 탈퇴일 기준
        else condition = rank.member.createdAt; // 가입일 기준

        if (sDate != null) {
            andBuilder.and(condition.after(sDate.atStartOfDay()));
        }

        if (eDate != null) {
            andBuilder.and(condition.before(eDate.atTime(LocalTime.of(23,59,59))));
        }

        // endregion

        // endregion

        List<GameRank> items = queryFactory.selectFrom(rank)
                .leftJoin(rank.member)
                .fetchJoin()
                .where(andBuilder)
                .orderBy(rank.gameCorrectAnswer.desc())
                .offset(offset)
                .limit(limit)
                .fetch();

        long total = repository.count(andBuilder); // 총 회원 수
        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        return new ListData<>(items, pagination);
    }
}
