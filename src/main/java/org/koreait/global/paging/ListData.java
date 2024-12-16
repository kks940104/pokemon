package org.koreait.global.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 제네릭을 사용한 이유는 포켓몬 뿐만 아니라 다른것도 들어올 수 있도록 만드려고.
 * EX) 멤버조회, 게시판 등등..
 */
public class ListData<T> {
    private List<T> items; // 목록 데이터
    private Pagination pagination; // 페이징 기초 데이터
}
