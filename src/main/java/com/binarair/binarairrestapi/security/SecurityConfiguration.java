package com.binarair.binarairrestapi.security;
import com.binarair.binarairrestapi.config.PasswordEncoderConfiguration;
import com.binarair.binarairrestapi.service.impl.JwtTokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

    private final PasswordEncoderConfiguration passwordEncoderConfiguration;

    private final JwtTokenAuthRequestFilter jwtTokenAuthRequestFilter;

    private final JwtTokenAuthService jwtTokenAuthService;



    @Autowired
    public SecurityConfiguration(PasswordEncoderConfiguration passwordEncoderConfiguration, JwtTokenAuthRequestFilter jwtTokenAuthRequestFilter,
                                 JwtTokenAuthService jwtTokenAuthService) {
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
        this.jwtTokenAuthRequestFilter = jwtTokenAuthRequestFilter;
        this.jwtTokenAuthService = jwtTokenAuthService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtTokenAuthService).passwordEncoder(passwordEncoderConfiguration.passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/v1/auth/signin", "/api/v1/user/signup");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtTokenAuthRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

}
