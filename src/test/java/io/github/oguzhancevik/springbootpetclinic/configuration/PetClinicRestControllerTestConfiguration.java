package io.github.oguzhancevik.springbootpetclinic.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class PetClinicRestControllerTestConfiguration {

    @LocalServerPort
    private int port;

    private final String rootUri = "http://localhost:" + port;

    @Bean
    public TestRestTemplate testRestTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri(rootUri);
        return new TestRestTemplate(restTemplateBuilder);
    }

}
