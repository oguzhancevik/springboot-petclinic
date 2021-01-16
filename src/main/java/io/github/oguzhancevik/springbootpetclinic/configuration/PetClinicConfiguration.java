package io.github.oguzhancevik.springbootpetclinic.configuration;

import io.github.oguzhancevik.springbootpetclinic.properties.PetClinicProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class PetClinicConfiguration {

    private final PetClinicProperties petClinicProperties;

    @Autowired
    public PetClinicConfiguration(PetClinicProperties petClinicProperties) {
        this.petClinicProperties = petClinicProperties;
    }

    @PostConstruct
    public void init() {
        System.out.println("Display owners with pets: " + petClinicProperties.isDisplayOwnersWithPets());
    }
}
