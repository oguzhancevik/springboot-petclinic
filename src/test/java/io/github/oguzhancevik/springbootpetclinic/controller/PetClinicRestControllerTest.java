package io.github.oguzhancevik.springbootpetclinic.controller;

import io.github.oguzhancevik.springbootpetclinic.model.Owner;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PetClinicRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = restTemplate.withBasicAuth("admin", "my-secret-password");
    }

    @Test
    void testGetOwnerById() {
        ResponseEntity<Owner> response = restTemplate.getForEntity(restTemplate.getRootUri() + "/api/owner/1", Owner.class);
        MatcherAssert.assertThat(response.getStatusCodeValue(), Matchers.equalTo(200));
        MatcherAssert.assertThat(response.getBody().getId(), Matchers.equalTo(1L));
    }

    @Test
    void testGetOwnersByLastName() {
        ResponseEntity<List> response = restTemplate.getForEntity(restTemplate.getRootUri() + "/api/owner?ln=Marquez", List.class);
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        List<Map<String, String>> body = response.getBody();
        List<String> firstNames = body.stream().map(e -> e.get("firstName")).collect(Collectors.toList());
        MatcherAssert.assertThat(firstNames, Matchers.containsInAnyOrder("Arturo", "Andres"));
    }

    @Test
    void testGetOwners() {
        ResponseEntity<List> response = restTemplate.getForEntity(restTemplate.getRootUri() + "/api/owners", List.class);
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        List<Map<String, String>> body = response.getBody();
        List<String> lastNames = body.stream().map(e -> e.get("lastName")).collect(Collectors.toList());
        MatcherAssert.assertThat(lastNames, Matchers.containsInRelativeOrder("Marquez", "Tyler"));
    }

    @Test
    void testCreateOwner() {
        Owner owner = new Owner("Gregory", "Alvarado");

        URI location = restTemplate.postForLocation(restTemplate.getRootUri() + "/api/owner", owner);
        Owner owner2 = restTemplate.getForObject(location, Owner.class);

        MatcherAssert.assertThat(owner2.getFirstName(), Matchers.equalTo(owner.getFirstName()));
        MatcherAssert.assertThat(owner2.getLastName(), Matchers.equalTo(owner.getLastName()));
    }

    @Test
    void testUpdateOwner() {
        Owner owner = restTemplate.getForObject(restTemplate.getRootUri() + "/api/owner/1", Owner.class);
        MatcherAssert.assertThat(owner.getFirstName(), Matchers.equalTo("John"));
        MatcherAssert.assertThat(owner.getLastName(), Matchers.equalTo("Doe"));
        owner.setFirstName("Jasmine");
        owner.setLastName("Sharp");
        restTemplate.put(restTemplate.getRootUri() + "/api/owner/1", owner);
        Owner owner2 = restTemplate.getForObject(restTemplate.getRootUri() + "/api/owner/1", Owner.class);
        MatcherAssert.assertThat(owner.getFirstName(), Matchers.equalTo(owner2.getFirstName()));
        MatcherAssert.assertThat(owner.getLastName(), Matchers.equalTo(owner2.getLastName()));
    }

    @Test
    void testDeleteOwner() {
        ResponseEntity<Void> response = restTemplate.exchange(restTemplate.getRootUri() + "/api/owner/1", HttpMethod.DELETE, null, Void.class);
        MatcherAssert.assertThat(response.getStatusCodeValue(), Matchers.equalTo(200));
        ResponseEntity<Owner> response2 = restTemplate.getForEntity(restTemplate.getRootUri() + "/api/owner/1", Owner.class);
        MatcherAssert.assertThat(response2.getStatusCodeValue(), Matchers.equalTo(404));
    }

    @Test
    void shouldThrowExceptionWhenAccessByUserWhoDoesNotHaveAdminRole() {
        assertThrows(Exception.class, () ->
                restTemplate.withBasicAuth("user1", "my-secret-password")
                        .getForEntity(restTemplate.getRootUri() + "/api/owners", List.class));

        assertThrows(Exception.class, () ->
                restTemplate.withBasicAuth("user2", "my-secret-password")
                        .getForEntity(restTemplate.getRootUri() + "/api/owners", List.class));
    }

}
