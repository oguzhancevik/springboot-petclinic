package io.github.oguzhancevik.springbootpetclinic.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
@Data
public class Authority extends BaseModel {

    @Column(nullable = false)
    private String authority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
