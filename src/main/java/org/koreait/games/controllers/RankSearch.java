package org.koreait.games.controllers;

import lombok.Data;
import org.koreait.global.paging.CommonSearch;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class RankSearch extends CommonSearch {
    private List<String> email;
    private String dateType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eDate;
}
