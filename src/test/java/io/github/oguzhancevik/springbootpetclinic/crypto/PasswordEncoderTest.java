package io.github.oguzhancevik.springbootpetclinic.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PasswordEncoderTest {

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeAll
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    public void testGenerateEncodedPassword() {
        System.out.println("{bcrypt}" + passwordEncoder.encode("my-secret-password"));
        System.out.println("{bcrypt}" + passwordEncoder.encode("my-secret-password"));
        System.out.println("{bcrypt}" + passwordEncoder.encode("my-secret-password"));
    }

}
