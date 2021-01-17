package io.github.oguzhancevik.springbootpetclinic.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
public class Pet extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Date birthDate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

}
