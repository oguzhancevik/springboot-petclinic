package io.github.oguzhancevik.springbootpetclinic.controller;

import io.github.oguzhancevik.springbootpetclinic.model.Owner;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetClinicRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${spring.security.user.name}")
    private String USER_NAME;

    @Value("${spring.security.user.password}")
    private String PASSWORD;

    @BeforeEach
    public void setUp() {
        restTemplate = restTemplate.withBasicAuth(USER_NAME, PASSWORD);
    }

    @Test
    @Order(1)
    public void testGetOwnerById() {
        ResponseEntity<Owner> response = restTemplate.getForEntity(restTemplate.getRootUri() + "/api/owner/1", Owner.class);
        MatcherAssert.assertThat(response.getStatusCodeValue(), Matchers.equalTo(200));
        MatcherAssert.assertThat(response.getBody().getId(), Matchers.equalTo(1L));
    }

    @Test
    @Order(2)
    public void testGetOwnersByLastName() {
        ResponseEntity<List> response = restTemplate.getForEntity(restTemplate.getRootUri() + "/api/owner?ln=Marquez", List.class);
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        List<Map<String, String>> body = response.getBody();
        List<String> firstNames = body.stream().map(e -> e.get("firstName")).collect(Collectors.toList());
        MatcherAssert.assertThat(firstNames, Matchers.containsInAnyOrder("Arturo", "Andres"));
    }

    @Test
    @Order(3)
    public void testGetOwners() {
        ResponseEntity<List> response = restTemplate.getForEntity(restTemplate.getRootUri() + "/api/owners", List.class);
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        List<Map<String, String>> body = response.getBody();
        List<String> lastNames = body.stream().map(e -> e.get("lastName")).collect(Collectors.toList());
        MatcherAssert.assertThat(lastNames, Matchers.containsInRelativeOrder("Marquez", "Tyler"));
    }

    @Test
    @Order(4)
    public void testCreateOwner() {
        Owner owner = new Owner();
        owner.setFirstName("Gregory");
        owner.setLastName("Alvarado");

        URI location = restTemplate.postForLocation(restTemplate.getRootUri() + "/api/owner", owner);
        Owner owner2 = restTemplate.getForObject(location, Owner.class);

        MatcherAssert.assertThat(owner2.getFirstName(), Matchers.equalTo(owner.getFirstName()));
        MatcherAssert.assertThat(owner2.getLastName(), Matchers.equalTo(owner.getLastName()));
    }

    @Test
    @Order(5)
    public void testUpdateOwner() {
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
    @Order(6)
    public void testDeleteOwner() {
        ResponseEntity<Void> response = restTemplate.exchange(restTemplate.getRootUri() + "/api/owner/1", HttpMethod.DELETE, null, Void.class);
        MatcherAssert.assertThat(response.getStatusCodeValue(), Matchers.equalTo(200));
        ResponseEntity<Owner> response2 = restTemplate.getForEntity(restTemplate.getRootUri() + "/api/owner/1", Owner.class);
        MatcherAssert.assertThat(response2.getStatusCodeValue(), Matchers.equalTo(404));
    }

}
