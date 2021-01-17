package io.github.oguzhancevik.springbootpetclinic;

import io.github.oguzhancevik.springbootpetclinic.properties.PetClinicProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties(value = PetClinicProperties.class)
@EnableJpaAuditing(auditorAwareRef = "petClinicAuditorAware")
public class PetClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetClinicApplication.class, args);
    }

}
