package io.github.oguzhancevik.springbootpetclinic.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(@Qualifier("userService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**/favicon.ico", "/css/**", "js/**", "/images/**", "/login").permitAll();

        http.authorizeRequests().antMatchers("/actuator/**").hasRole("ADMIN");

        http.authorizeRequests().anyRequest().authenticated();

        http.formLogin().loginPage("/login").failureUrl("/login?loginFailed=true");

        http.rememberMe().userDetailsService(userDetailsService);
    }
}
