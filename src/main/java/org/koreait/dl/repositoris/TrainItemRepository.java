package org.koreait.dl.repositoris;

import org.koreait.dl.entities.TrainItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TrainItemRepository extends JpaRepository<TrainItem, Long> , QuerydslPredicateExecutor<TrainItem> {
}
