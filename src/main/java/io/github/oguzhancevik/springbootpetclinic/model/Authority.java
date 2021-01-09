package io.github.oguzhancevik.springbootpetclinic.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
@Data
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "petClinicAuthoritySeqGen")
    @SequenceGenerator(name = "petClinicAuthoritySeqGen", sequenceName = "petclinic_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String authority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
