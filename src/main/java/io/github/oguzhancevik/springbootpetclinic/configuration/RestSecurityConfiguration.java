package io.github.oguzhancevik.springbootpetclinic.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(value = 1)
public class RestSecurityConfiguration  extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**");
        http.authorizeRequests().antMatchers("/api/**").hasAnyRole("ADMIN", "EDITOR");
        http.httpBasic();
        http.csrf().disable();
    }

}
