package org.koreait.dl.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.koreait.global.entities.BaseEntity;


/**
 * 학습 데이터들.
 * Lombok에 Builder 패턴 사용.
 * NoArgsConstructor와 AllArgsConstructor 사용해야 Builder 패턴 사용 가능.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor // 빌더 패턴일 때 기본 생성자가 접근 가능해야 하는 경우
public class TrainItem extends BaseEntity {
    @Id @GeneratedValue
    private Long seq;
    private int item1;
    private int item2;
    private int item3;
    private int item4;
    private int item5;
    private int item6;
    private int item7;
    private int item8;
    private int item9;
    private int item10;
    private int result; // 무조건적인 정수 범위 사용. 0 ~ 4
}
