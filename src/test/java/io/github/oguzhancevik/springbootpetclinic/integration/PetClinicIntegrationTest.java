package io.github.oguzhancevik.springbootpetclinic.integration;

import io.github.oguzhancevik.springbootpetclinic.exception.OwnerNotFoundException;
import io.github.oguzhancevik.springbootpetclinic.model.Owner;
import io.github.oguzhancevik.springbootpetclinic.service.PetClinicService;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PetClinicIntegrationTest {

    @Autowired
    private PetClinicService petClinicService;

    @Test
    @Order(1)
    void testFindOwners() {
        List<Owner> owners = petClinicService.findOwners();
        MatcherAssert.assertThat(owners.size(), Matchers.equalTo(4));
        Assertions.assertThat(owners).extracting("firstName")
                .contains("John", "Arturo", "Lance", "Andres").doesNotContain("Gianna");
        Assertions.assertThat(owners).extracting("lastName")
                .contains("Doe", "Marquez", "Tyler").doesNotContain("Romero");
    }

    @Test
    @Order(2)
    void testFindOwnersByLastName() {
        List<Owner> owners = petClinicService.findOwners("Marquez");
        MatcherAssert.assertThat(owners.size(), Matchers.equalTo(2));
        Assertions.assertThat(owners).extracting("firstName")
                .contains("Arturo", "Andres").doesNotContain("John", "Lance");
        Assertions.assertThat(owners).extracting("lastName")
                .contains("Marquez").doesNotContain("Doe", "Tyler");
    }

    @Test
    @Order(3)
    void testFindOwnerById() {
        Owner owner = petClinicService.findOwner(1L);
        MatcherAssert.assertThat(owner.getFirstName(), Matchers.equalTo("John"));
        MatcherAssert.assertThat(owner.getLastName(), Matchers.equalTo("Doe"));
    }

    @Test
    @Order(4)
    void shouldThrowOwnerNotFoundExceptionWhenOwnerNotFound() {
        assertThrows(OwnerNotFoundException.class, () -> petClinicService.findOwner(-1L));
    }

    @Test
    @Order(5)
    void testCreateOwner() {
        Owner owner = new Owner();
        owner.setFirstName("Lydia");
        owner.setLastName("Quinn");
        petClinicService.createOwner(owner);
        Owner owner2 = petClinicService.findOwner(owner.getId());
        MatcherAssert.assertThat(owner2.getFirstName(), Matchers.equalTo(owner.getFirstName()));
        MatcherAssert.assertThat(owner2.getLastName(), Matchers.equalTo(owner.getLastName()));
    }

    @Test
    @Order(6)
    void testUpdateOwner() {
        Owner owner = petClinicService.findOwner(1L);
        MatcherAssert.assertThat(owner.getFirstName(), Matchers.equalTo("John"));
        MatcherAssert.assertThat(owner.getLastName(), Matchers.equalTo("Doe"));
        owner.setFirstName("Stefanie");
        owner.setLastName("Pope");
        petClinicService.updateOwner(owner);
        Owner owner2 = petClinicService.findOwner(1L);
        MatcherAssert.assertThat(owner2.getFirstName(), Matchers.equalTo(owner.getFirstName()));
        MatcherAssert.assertThat(owner2.getLastName(), Matchers.equalTo(owner.getLastName()));
    }

    @Test
    @Order(7)
    void testDeleteOwner() {
        petClinicService.deleteOwner(1L);
        assertThrows(OwnerNotFoundException.class, () -> petClinicService.findOwner(1L));
    }

    @Test
    @Order(8)
    void shouldThrowEmptyResultDataAccessExceptionWhenDelete() {
        assertThrows(EmptyResultDataAccessException.class, () -> petClinicService.deleteOwner(-1L));
    }

}
