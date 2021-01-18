package io.github.oguzhancevik.springbootpetclinic.service;

import io.github.oguzhancevik.springbootpetclinic.exception.OwnerNotFoundException;
import io.github.oguzhancevik.springbootpetclinic.model.Owner;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PetClinicServiceTest {

    @Autowired
    private PetClinicService petClinicService;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        TestingAuthenticationToken token =
                new TestingAuthenticationToken("admin", "my-secret-password", "ROLE_ADMIN");
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testFindOwners() {
        List<Owner> owners = petClinicService.findOwners();
        MatcherAssert.assertThat(owners.size(), Matchers.equalTo(4));
        Assertions.assertThat(owners).extracting("firstName")
                .contains("John", "Arturo", "Lance", "Andres").doesNotContain("Gianna");
        Assertions.assertThat(owners).extracting("lastName")
                .contains("Doe", "Marquez", "Tyler").doesNotContain("Romero");
    }

    @Test
    void testFindOwnersByLastName() {
        List<Owner> owners = petClinicService.findOwners("Marquez");
        MatcherAssert.assertThat(owners.size(), Matchers.equalTo(2));
        Assertions.assertThat(owners).extracting("firstName")
                .contains("Arturo", "Andres").doesNotContain("John", "Lance");
        Assertions.assertThat(owners).extracting("lastName")
                .contains("Marquez").doesNotContain("Doe", "Tyler");
    }

    @Test
    void testFindOwnerById() {
        Owner owner = petClinicService.findOwner(1L);
        MatcherAssert.assertThat(owner.getFirstName(), Matchers.equalTo("John"));
        MatcherAssert.assertThat(owner.getLastName(), Matchers.equalTo("Doe"));
    }

    @Test
    void shouldThrowOwnerNotFoundExceptionWhenOwnerNotFound() {
        assertThrows(OwnerNotFoundException.class, () -> petClinicService.findOwner(-1L));
    }

    @Test
    void testCreateOwner() {
        Owner owner = new Owner("Lydia", "Quinn");
        petClinicService.saveOwner(owner);
        Owner owner2 = petClinicService.findOwner(owner.getId());
        MatcherAssert.assertThat(owner2.getFirstName(), Matchers.equalTo(owner.getFirstName()));
        MatcherAssert.assertThat(owner2.getLastName(), Matchers.equalTo(owner.getLastName()));
    }

    @Test
    void shouldThrownConstraintViolationExceptionWhenOwnerNameIsNullOrOwnerLastNameIsNull() {
        Owner owner = new Owner(null, null);
        petClinicService.saveOwner(owner);
        assertThrows(ConstraintViolationException.class, () -> entityManager.flush());
    }

    @Test
    void testUpdateOwner() {
        Owner owner = petClinicService.findOwner(1L);
        MatcherAssert.assertThat(owner.getFirstName(), Matchers.equalTo("John"));
        MatcherAssert.assertThat(owner.getLastName(), Matchers.equalTo("Doe"));
        owner.setFirstName("Stefanie");
        owner.setLastName("Pope");
        petClinicService.saveOwner(owner);
        Owner owner2 = petClinicService.findOwner(1L);
        MatcherAssert.assertThat(owner2.getFirstName(), Matchers.equalTo(owner.getFirstName()));
        MatcherAssert.assertThat(owner2.getLastName(), Matchers.equalTo(owner.getLastName()));
    }

    @Test
    void testDeleteOwner() {
        petClinicService.deleteOwner(1L);
        assertThrows(OwnerNotFoundException.class, () -> petClinicService.findOwner(1L));
    }

    @Test
    void shouldThrowEmptyResultDataAccessExceptionWhenDelete() {
        assertThrows(EmptyResultDataAccessException.class, () -> petClinicService.deleteOwner(-1L));
    }

}
