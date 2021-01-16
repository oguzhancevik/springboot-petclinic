package io.github.oguzhancevik.springbootpetclinic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testLoadUserByUsername() {
        UserDetails user = userService.loadUserByUsername("admin");
        Assertions.assertEquals("admin", user.getUsername());

        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) user.getAuthorities();
        Assertions.assertEquals(3, authorities.size());
        org.assertj.core.api.Assertions.assertThat(authorities).extracting("authority")
                .contains("ROLE_USER", "ROLE_EDITOR", "ROLE_ADMIN").doesNotContain("ROLE_XYZ");

        user = userService.loadUserByUsername("user1");
        Assertions.assertEquals("user1", user.getUsername());
        authorities = (Collection<GrantedAuthority>) user.getAuthorities();
        Assertions.assertEquals(1, authorities.size());
        org.assertj.core.api.Assertions.assertThat(authorities).extracting("authority")
                .contains("ROLE_USER").doesNotContain("ROLE_EDITOR", "ROLE_ADMIN");

        user = userService.loadUserByUsername("user2");
        Assertions.assertEquals("user2", user.getUsername());
        authorities = (Collection<GrantedAuthority>) user.getAuthorities();
        Assertions.assertEquals(2, authorities.size());
        org.assertj.core.api.Assertions.assertThat(authorities).extracting("authority")
                .contains("ROLE_USER", "ROLE_EDITOR").doesNotContain("ROLE_ADMIN");
    }

    @Test
    void shouldUsernameNotFoundExceptionWhenUsernameNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("invalid-username"));
    }

}
