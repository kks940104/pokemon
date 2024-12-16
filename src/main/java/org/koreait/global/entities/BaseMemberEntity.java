package org.koreait.global.entities;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass // 공통 속성 엔티티라는걸 상위클래스에게 알려주는거.
@EntityListeners(AuditingEntityListener.class) // 이벤트리스너가 들어감. 엔티티가 변화하는지 계속 지켜보고있음.
/**
 * 다형성 쿼리를 위해 사용.
 */
public abstract class BaseMemberEntity extends BaseEntity {

    @CreatedBy
    @Column(length = 65, updatable = false)
    private String createdBy; // 작성자

    @LastModifiedBy
    @Column(length = 65, insertable = false)
    private String modifiedBy; // 수정한자
}
