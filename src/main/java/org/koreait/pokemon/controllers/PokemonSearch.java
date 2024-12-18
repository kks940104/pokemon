package org.koreait.pokemon.controllers;

import lombok.Data;
import org.koreait.global.paging.CommonSearch;

import java.util.List;

/**
 * 검색하는 것이 더 있을 경우 여기서 추가.
 * CommonSearch 상속 받음.
 * 
 */
@Data
public class PokemonSearch extends CommonSearch {
    private List<Long> seq;
}
