package org.koreait.games.services;

import lombok.RequiredArgsConstructor;
import org.koreait.games.constants.Level;
import org.koreait.games.controllers.RequestShadowGame;
import org.koreait.games.entitis.GameRank;
import org.koreait.games.entitis.GameRankId;
import org.koreait.games.repositories.ShadowGameRepository;
import org.koreait.member.libs.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class ShadowGameRankUpdateService {

    private final ShadowGameRepository repository;
    private final ModelMapper modelMapper;
    private final MemberUtil memberUtil;

    public void process(RequestShadowGame form) {
        GameRank gameRank = modelMapper.map(form, GameRank.class);
        gameRank.setMember(memberUtil.getMember());

        gameLevel(form, gameRank);

        repository.saveAndFlush(gameRank);
    }

    private void gameLevel(RequestShadowGame form, GameRank gameRank) {
        if (form.isRow()) {
            gameRank.setLevel(Level.ROW);
        } else if (form.isMid()) {
            gameRank.setLevel(Level.MID);
        } else {
            gameRank.setLevel(Level.HIGH);
        }
    }
}
