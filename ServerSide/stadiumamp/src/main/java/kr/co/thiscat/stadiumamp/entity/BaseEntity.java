package kr.co.thiscat.stadiumamp.entity;


import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public abstract class BaseEntity {
    //base Entity

   /* @CreatedBy
    @Column(updatable = false)
    private String regUser;

    @LastModifiedBy
    private String edtUser;*/

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime regDateTime;

    @UpdateTimestamp
    private LocalDateTime edtDateTime;


}