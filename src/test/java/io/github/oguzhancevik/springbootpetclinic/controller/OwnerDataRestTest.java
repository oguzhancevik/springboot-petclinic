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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OwnerDataRestTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String baseRequestMapping = "/api/v2/owners";

    @BeforeEach
    void setUp() {
        restTemplate = restTemplate.withBasicAuth("admin", "my-secret-password");
    }

    @Test
    void testGetOwnerById() {
        ResponseEntity<Owner> response = restTemplate.getForEntity(restTemplate.getRootUri() + baseRequestMapping + "/1", Owner.class);
        MatcherAssert.assertThat(response.getStatusCodeValue(), Matchers.equalTo(200));
        MatcherAssert.assertThat(response.getBody().getFirstName(), Matchers.equalTo("John"));
        MatcherAssert.assertThat(response.getBody().getLastName(), Matchers.equalTo("Doe"));
    }

    @Test
    void testGetOwnersByLastName() {
        String uri = restTemplate.getRootUri() + baseRequestMapping + "/search/findByLastName?ln=Marquez";
        ResponseEntity<Object> response = restTemplate.getForEntity(uri, Object.class);
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));

        Map<String, List<String>> body = (Map<String, List<String>>) response.getBody();
        Map<String, Map<String, List<String>>> embedded = (Map<String, Map<String, List<String>>>) body.get("_embedded");
        List<Map<String, String>> owners = (List<Map<String, String>>) embedded.get("owners");

        MatcherAssert.assertThat(owners.size(), Matchers.equalTo(2));
        MatcherAssert.assertThat(owners.get(0).get("firstName"), Matchers.equalTo("Arturo"));
        MatcherAssert.assertThat(owners.get(0).get("lastName"), Matchers.equalTo("Marquez"));
        MatcherAssert.assertThat(owners.get(1).get("firstName"), Matchers.equalTo("Andres"));
        MatcherAssert.assertThat(owners.get(1).get("lastName"), Matchers.equalTo("Marquez"));
    }

    @Test
    void testGetOwners() {
        ResponseEntity<Object> response = restTemplate.getForEntity(restTemplate.getRootUri() + baseRequestMapping, Object.class);
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));

        Map<String, List<String>> body = (Map<String, List<String>>) response.getBody();
        Map<String, Map<String, List<String>>> embedded = (Map<String, Map<String, List<String>>>) body.get("_embedded");
        List<Map<String, String>> owners = (List<Map<String, String>>) embedded.get("owners");

        List<String> firstNames = owners.stream().map(e -> e.get("firstName")).collect(Collectors.toList());
        MatcherAssert.assertThat(firstNames, Matchers.containsInRelativeOrder("John", "Arturo", "Lance", "Andres"));

        List<String> lastNames = owners.stream().map(e -> e.get("lastName")).collect(Collectors.toList());
        MatcherAssert.assertThat(lastNames, Matchers.containsInRelativeOrder("Marquez", "Tyler", "Marquez"));

        response = restTemplate.withBasicAuth("user1", "my-secret-password")
                .getForEntity(restTemplate.getRootUri() + baseRequestMapping, Object.class);
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.FORBIDDEN));
    }

    @Test
    void testCreateOwner() {
        Owner owner = new Owner("Gregory", "Alvarado");

        URI location = restTemplate.postForLocation(restTemplate.getRootUri() + baseRequestMapping, owner);
        Owner owner2 = restTemplate.getForObject(location, Owner.class);

        MatcherAssert.assertThat(owner2.getFirstName(), Matchers.equalTo(owner.getFirstName()));
        MatcherAssert.assertThat(owner2.getLastName(), Matchers.equalTo(owner.getLastName()));
        MatcherAssert.assertThat(owner2.getCreatedBy(), Matchers.equalTo("admin"));
    }

    @Test
    void testUpdateOwner() {
        Owner owner = restTemplate.getForObject(restTemplate.getRootUri() + baseRequestMapping + "/1", Owner.class);
        MatcherAssert.assertThat(owner.getFirstName(), Matchers.equalTo("John"));
        MatcherAssert.assertThat(owner.getLastName(), Matchers.equalTo("Doe"));
        owner.setFirstName("Jasmine");
        owner.setLastName("Sharp");
        restTemplate.put(restTemplate.getRootUri() + baseRequestMapping + "/1", owner);
        Owner owner2 = restTemplate.getForObject(restTemplate.getRootUri() + baseRequestMapping + "/1", Owner.class);
        MatcherAssert.assertThat(owner.getFirstName(), Matchers.equalTo(owner2.getFirstName()));
        MatcherAssert.assertThat(owner.getLastName(), Matchers.equalTo(owner2.getLastName()));
        MatcherAssert.assertThat(owner2.getUpdatedBy(), Matchers.equalTo("admin"));
        MatcherAssert.assertThat(owner2.getUpdatedDate(), Matchers.notNullValue());
    }

    @Test
    void testDeleteOwner() {
        ResponseEntity<Void> response = restTemplate.exchange(restTemplate.getRootUri() + baseRequestMapping + "/1", HttpMethod.DELETE, null, Void.class);
        MatcherAssert.assertThat(response.getStatusCodeValue(), Matchers.equalTo(204));
        ResponseEntity<Owner> response2 = restTemplate.getForEntity(restTemplate.getRootUri() + baseRequestMapping + "/1", Owner.class);
        MatcherAssert.assertThat(response2.getStatusCodeValue(), Matchers.equalTo(404));
    }

}
