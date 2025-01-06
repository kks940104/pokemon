package org.koreait.message.entities;

import jakarta.persistence.*;
import lombok.*;
import org.koreait.file.entites.FileInfo;
import org.koreait.global.entities.BaseEntity;
import org.koreait.member.entities.Member;
import org.koreait.message.constants.MessageStatus;

import java.util.List;

@Data
@Entity
@Builder
// 빌더를 쓰면 기본 생성자가 private라서 편법을 사용하기 위해 두가지 추가.
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name="idx_notice_created_at", columnList = "notice DESC, createdAt DESC"))
public class Message extends BaseEntity {

    @Id
    @GeneratedValue
    private Long seq;

    private boolean notice; // 공지 항상 상단쪽에 노출시킴.

    @Column(length = 45, nullable = false)
    private String gid;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private MessageStatus status;

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
    private String content; // 내용

    @Transient
    private List<FileInfo> editorImages;

    @Transient
    private List<FileInfo> attachFiles;

    @Transient
    private boolean received;

    @Transient
    private boolean deletable; // 삭제 가능 여부
    
    private boolean deletedBySender; // 보내는 쪽에서 쪽지를 삭제한 경우

    private boolean deletedByReceiver; // 받는 쪽에서 쪽지를 삭제한 경우
}










