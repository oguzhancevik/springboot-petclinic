package io.github.oguzhancevik.springbootpetclinic.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**/favicon.ico", "/css/**", "js/**", "/images/**", "/login").permitAll()
                .anyRequest().authenticated();

        http.formLogin().loginPage("/login").failureUrl("/login?loginFailed=true");
    }
}
