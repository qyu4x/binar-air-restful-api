package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.config.JwtTokenConfiguration;
import com.binarair.binarairrestapi.model.entity.Role;
import com.binarair.binarairrestapi.model.entity.User;
import com.binarair.binarairrestapi.model.request.UserAuthRequest;
import com.binarair.binarairrestapi.model.response.UserAuthResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.impl.JwtTokenAuthService;
import com.binarair.binarairrestapi.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    private final Logger log = LoggerFactory.getLogger(UserAuthController.class);

    private final JwtTokenAuthService jwtTokenAuthService;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtTokenConfiguration jwtTokenConfiguration;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserAuthController(JwtTokenAuthService jwtTokenAuthService, JwtTokenUtil jwtTokenUtil, JwtTokenConfiguration jwtTokenConfiguration, AuthenticationManager authenticationManager) {
        this.jwtTokenAuthService = jwtTokenAuthService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtTokenConfiguration = jwtTokenConfiguration;
        this.authenticationManager = authenticationManager;
    }


    @Operation(summary = "carry out the sign-in process")
    @PostMapping("/signin")
    public ResponseEntity<WebResponse<UserAuthResponse>> createJwtToken(@RequestBody @Valid UserAuthRequest userAuthRequest) {
        log.info("Calling login role");
        authenticate(userAuthRequest.getEmail(), userAuthRequest.getPassword());
        log.info("Login succeess user account {} ", userAuthRequest.getEmail());
        UserDetails userDetails = jwtTokenAuthService.loadUserByUsername(userAuthRequest.getEmail());
        log.info("Load user {} details success", userDetails.getUsername());
        String jwtToken = jwtTokenUtil.generateToken(userDetails);

        User user = jwtTokenAuthService.findByEmail(userAuthRequest.getEmail());

        UserAuthResponse userAuthResponse = UserAuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().getRole().name())
                .tokenType(jwtTokenConfiguration.getTokenPrefix())
                .jwtToken(jwtToken)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        log.info("Success create token");
        WebResponse<UserAuthResponse> webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                userAuthResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.OK);

    }

    public void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (DisabledException exception) {
            log.error("User account {} is disabled", email);
            throw new DisabledException("account is disabled");
        } catch (BadCredentialsException exception) {
            log.error("Username or password is wrong for user account {} ", email);
            throw new BadCredentialsException("Email or password is wrong");
        }
    }
}
