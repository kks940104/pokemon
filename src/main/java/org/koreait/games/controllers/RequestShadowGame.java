package org.koreait.games.controllers;

import lombok.Data;
import org.koreait.pokemon.entities.Pokemon;

import java.io.Serializable;
import java.util.List;

@Data
public class RequestShadowGame implements Serializable {

    private List<Long> pokemonCount; // 현재 난이도에 맞게 포켓몬을 넣어주는 List

    private Long pokemonCheck; // 상 - 898, 중 - 649, 하 - 493으로 포켓몬 몇명 뽑았는지 체크
    private boolean row; // 난이도 하
    private boolean mid; // 난이도 중
    private boolean high; // 난이도 상

    private String pokemonName; // 입력한 포켓몬 이름

    private int gameCount; // 현재 게임 Count
    private Pokemon gamePokemon;

    private boolean gameQuizAnswer; // 정답 맞췄는지 못맞췄는지 확인. 이거 지워질 확률 있음.
    private int gameCorrectAnswer; // 정답 갯수
    private int gameWrongAnswer; // 오답 갯수
    private boolean gameBtnClick; // 정답을 눌렀는지 안눌렀는지 체크
}
