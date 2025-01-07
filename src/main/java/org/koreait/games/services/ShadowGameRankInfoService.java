package org.koreait.games.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.koreait.games.constants.Level;
import org.koreait.games.entitis.GameRank;
import org.koreait.games.entitis.GameRankId;
import org.koreait.games.entitis.QGameRank;
import org.koreait.games.repositories.ShadowGameRepository;
import org.koreait.member.libs.MemberUtil;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import java.util.List;


@Lazy
@Service
@RequiredArgsConstructor
public class ShadowGameRankInfoService {

    private final JPAQueryFactory queryFactory;
    private final ShadowGameRepository repository;
    private final MemberUtil memberUtil;

    public int getRank(Level level) {
        QGameRank gameRank = QGameRank.gameRank;
        BooleanBuilder builder = new BooleanBuilder();
        List<GameRank> rank = conditionRank(builder, gameRank, level);

        GameRankId gameRankId = new GameRankId(memberUtil.getMember(), level);
        GameRank user = repository.findById(gameRankId).orElseGet(null);

        if (user != null) {
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
}
