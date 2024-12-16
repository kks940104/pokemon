package org.koreait.pokemon.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.koreait.global.entities.BaseEntity;

import java.util.List;
import java.util.Map;

@Data
@Entity
public class Pokemon extends BaseEntity {

    // wrapper class 사용
    @Id
    private Long seq;

/*    private int pokeId;*/

    @Column(length=50)
    // 포켓몬 이름 기본값(한글)
    private String name;

    // 포켓몬 이름(영어)
    @Column(length=50)
    private String nameEn;

    private int weight;

    private int height;

    private int baseExperience;

    private String hp;
    private String attack;
    private String defense;
    private String specialAttack;
    private String specialDefense;
    private String speed;

    /*
    기본 데이터형일땐 not null 이 붙음 래퍼 클래스 형태의 Integer Long 을 사용시엔 not null 이 안붙음 필수가 아닌 경우 기본형이 아닌 래퍼 클래스로 정의하면 됨.
     */

    private String frontImage;

    // 포켓몬 설명 Text
    @Lob
    private String flavorText;

    // Type1 || Type2
    private String types;

    // 특성1 || 특성2 || 특성3
    private String abilities;

    @Column(length=100)
    private String genus; // 분류

    @Transient
    private List<String> _types; // 타입 가공된 데이터

    @Transient
    private List<String> _abilities; // 특성 가공된 데이터

    @Transient
    private Map<String, Object> prevItem;

    @Transient
    private Map<String, Object> nextItem;

    @ToString.Exclude
    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.REMOVE)
    private List<DataByVersion> items;

}