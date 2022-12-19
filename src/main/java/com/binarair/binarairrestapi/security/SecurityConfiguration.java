package com.binarair.binarairrestapi.security;

import com.binarair.binarairrestapi.config.PasswordEncoderConfiguration;
import com.binarair.binarairrestapi.oauth.CustomOAuth2User;
import com.binarair.binarairrestapi.service.CustomOauth2UserService;
import com.binarair.binarairrestapi.service.UserService;
import com.binarair.binarairrestapi.service.impl.JwtTokenAuthService;
import com.binarair.binarairrestapi.service.impl.UserServiceImpl;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfiguration  {
// extends WebSecurityConfigurerAdapter
//    private final PasswordEncoderConfiguration passwordEncoderConfiguration;
//
//    private final JwtTokenAuthRequestFilter jwtTokenAuthRequestFilter;
//
//    private final JwtTokenAuthService jwtTokenAuthService;


    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private CustomOauth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/google", "/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .oauth2Login()
                .userInfoEndpoint().userService(oAuth2UserService)
                .and()
                .successHandler((request, response, authentication) -> {
                    CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                    userServiceImpl.processOAuthPostLogin(oauthUser.getEmail(), oauthUser.getName());
                    response.sendRedirect("/api/v1/city/all");
                })
                .and()
                .logout().permitAll();
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }
//    @Autowired
//    public SecurityConfiguration(PasswordEncoderConfiguration passwordEncoderConfiguration, JwtTokenAuthRequestFilter jwtTokenAuthRequestFilter,
//                                 JwtTokenAuthService jwtTokenAuthService) {
//        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
//        this.jwtTokenAuthRequestFilter = jwtTokenAuthRequestFilter;
//        this.jwtTokenAuthService = jwtTokenAuthService;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(jwtTokenAuthService).passwordEncoder(passwordEncoderConfiguration.passwordEncoder());
//    }
//
//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/oauth","/swagger-ui/**", "/v3/api-docs/**", "/api/v1/auth/signin", "/api/v1/user/signup", "/api/v1/herobanner/all",
//        "/api/v1/promobanner/all", "/api/v1/promobanner", "/api/v1/titel/all", "/api/v1/titel", "/api/v1/city/all", "/api/v1/city", "/api/v1/country", "/api/v1/country/all",
//        "/api/v1/airport", "/api/v1/airport/all", "/api/v1/flight/**", "/api/v1/checkin/**","/api/v1/jasperreport/boardingpass/**", "/api/v1/travel/all", "/api/v1/travel", "/api/v1/agecategory", "/api/v1/agecategory/all");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .cors().and()
//                .csrf().disable()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.addFilterBefore(jwtTokenAuthRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//    }

}
