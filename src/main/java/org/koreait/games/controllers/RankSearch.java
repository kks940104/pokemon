package org.koreait.games.controllers;

import lombok.Data;
import org.koreait.games.constants.Level;
import org.koreait.global.paging.CommonSearch;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class RankSearch extends CommonSearch {
    private List<String> email;
    private Level level;
    private String nickName;
    private String name;
}
