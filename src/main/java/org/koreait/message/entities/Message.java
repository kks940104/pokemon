package org.koreait.message.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.global.entities.BaseEntity;
import org.koreait.member.entities.Member;

@Data
@Entity
public class Message extends BaseEntity {

    @Id
    @GeneratedValue
    private Long seq;

    private boolean notice; // 공지 항상 상단쪽에 노출시킴.

    @JoinColumn(name="sender")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender; // 보내는 사람

    @JoinColumn(name="receiver")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver; // 받는 사람

    @Column(length = 150, nullable = false)
    private String subject; // 제목

    @Lob
    @Column(nullable = false)
    private String content; // 제목
}
