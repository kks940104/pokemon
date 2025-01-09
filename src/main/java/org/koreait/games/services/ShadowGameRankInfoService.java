package org.koreait.games.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.koreait.games.constants.Level;
import org.koreait.games.controllers.RankSearch;
import org.koreait.games.entitis.GameRank;
import org.koreait.games.entitis.GameRankId;
import org.koreait.games.entitis.QGameRank;
import org.koreait.games.repositories.ShadowGameRepository;
import org.koreait.member.libs.MemberUtil;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;

import java.util.List;


@Lazy
@Service
@RequiredArgsConstructor
public class ShadowGameRankInfoService {

    private final JPAQueryFactory queryFactory;
    private final ShadowGameRepository repository;
    private final MemberUtil memberUtil;

    public int getRank(Level level, RankSearch search) {
        QGameRank gameRank = QGameRank.gameRank;
        BooleanBuilder builder = new BooleanBuilder();

        String sopt = search.getSopt(); // 검색 옵션
        String skey = search.getSkey(); // 검색 키워드
        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";

        if (StringUtils.hasText(skey)) {
            skey = skey.trim();
            StringExpression condition;
            if (sopt.equals("EMAIL")) {
                condition = gameRank.member.email;
            } else if (sopt.equals("NICKNAME")) {
                condition = gameRank.member.nickName;
            } else if (sopt.equals("NAME")) {
                condition = gameRank.member.name;
            } else {
                condition = gameRank.member.email.concat(gameRank.member.name).concat(gameRank.member.nickName);
            }

            builder.and(condition.contains(skey));
        }


        List<GameRank> rank = conditionRank(builder, gameRank, level);

        GameRankId gameRankId = new GameRankId(memberUtil.getMember(), level);
        GameRank user = repository.findById(gameRankId).orElseGet(null);

        if (user != null) {
            addInfo(user);
            return rank.indexOf(user) + 1;
        } else {
            return -1;
        }
    }

    public List<GameRank> getRankList(Level level) {
        QGameRank gameRank = QGameRank.gameRank;
        BooleanBuilder builder = new BooleanBuilder();
        List<GameRank> rank = conditionRank(builder, gameRank, level);
        int _rank = Math.min(rank.size(), 20);
        rank.forEach(this::addInfo);
        return conditionRank(builder, gameRank, level).stream().limit(_rank).toList();
    }

    public List<GameRank> conditionRank(BooleanBuilder builder, QGameRank gameRank, Level level) {
        builder.and(gameRank.level.eq(level));
        return queryFactory
                .selectFrom(gameRank)
                .where(builder)
                .orderBy(gameRank.gameCorrectAnswer.desc(), gameRank.createdAt.asc())
                .fetch();
    }

    private void addInfo(GameRank gameRank) {
        double winning = ((double) gameRank.getGameCorrectAnswer() / 20) * 100;
        gameRank.setWinning(winning);
    }
}
