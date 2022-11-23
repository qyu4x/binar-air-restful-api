package com.binarair.binarairrestapi.security;

import com.binarair.binarairrestapi.config.PasswordEncoderConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoderConfiguration passwordEncoderConfiguration;

    @Autowired
    public SecurityConfiguration(PasswordEncoderConfiguration passwordEncoderConfiguration) {
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("kaguya")
                .password(passwordEncoderConfiguration.passwordEncoder().encode("kanojo"))
                .authorities("ROLE_USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/v1/auth/login", "/api/v1/user/signup");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

    }

}
