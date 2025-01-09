package org.koreait.games.entitis;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.games.constants.Level;
import org.koreait.global.entities.BaseEntity;
import org.koreait.member.entities.Member;

@Data
@Entity
@IdClass(GameRankId.class)
public class GameRank extends BaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Member member;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(length = 20, name="_level")
    private Level level;

    private int gameCorrectAnswer;
    private int gameWrongAnswer;

    @Transient
    private int gameCount; // 20으로 고정될거임

    @Transient
    private double winning; // 승률 2차가공

    @Transient
    private int ranking;
}
