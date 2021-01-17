package io.github.oguzhancevik.springbootpetclinic.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "petClinicSeqGen")
    @SequenceGenerator(name = "petClinicSeqGen", sequenceName = "petclinic_sequence", allocationSize = 1)
    private Long id;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Date createdDate;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private Date updatedDate;

}
