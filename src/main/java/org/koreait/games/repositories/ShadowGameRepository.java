package org.koreait.games.repositories;

import org.koreait.games.entitis.GameRank;
import org.koreait.games.entitis.GameRankId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ShadowGameRepository extends JpaRepository<GameRank, GameRankId>, QuerydslPredicateExecutor<GameRank> {

}
