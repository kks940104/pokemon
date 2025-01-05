package org.koreait.games.entitis;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.koreait.games.constants.Level;
import org.koreait.member.entities.Member;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GameRankId {
    private Member member;
    private Level level;
}
