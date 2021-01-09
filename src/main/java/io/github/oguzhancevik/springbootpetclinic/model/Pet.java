package io.github.oguzhancevik.springbootpetclinic.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "petClinicPetSeqGen")
    @SequenceGenerator(name = "petClinicPetSeqGen", sequenceName = "petclinic_sequence",  allocationSize = 1)
    private Long id;

    private String name;

    private Date birthDate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

}
