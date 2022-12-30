package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Role;
import com.binarair.binarairrestapi.model.entity.User;
import com.binarair.binarairrestapi.model.enums.RoleType;
import com.binarair.binarairrestapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class JwtTokenAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtTokenAuthService jwtTokenAuthService;

    @Test
    void testGetAuthoritiesSuccess() {
        Role role = new Role();
        role.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-user-id")
                .fullName("kaguya chan")
                .email("kaguyachan@gmail.com")
                .password("rahasia")
                .role(role)
                .build();

        Set<SimpleGrantedAuthority> authorities = jwtTokenAuthService.getAuthorities(user);
        Assertions.assertEquals(1, authorities.size());

    }

    @Test
    void testLoadUserByUsernameSuccess() {
        String email = "kaguyachan@gmail.com";

        Role role = new Role();
        role.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-user-id")
                .fullName("kaguya chan")
                .email("kaguyachan@gmail.com")
                .password("rahasia")
                .role(role)
                .build();

        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = jwtTokenAuthService.loadUserByUsername(email);

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(email, userDetails.getUsername());

        Mockito.verify(userRepository).findByEmail(email);

    }

    @Test
    void testLoadUserByUsernameNotFound() {
        String email = "kaguyachan@gmail.com";

        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            jwtTokenAuthService.loadUserByUsername(email);
        });

        Mockito.verify(userRepository).findByEmail(email);
    }

    @Test
    void testFindByEmailSuccess() {
        String email = "kaguyachan@gmail.com";

        Role role = new Role();
        role.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-user-id")
                .fullName("kaguya chan")
                .email("kaguyachan@gmail.com")
                .password("rahasia")
                .role(role)
                .build();

        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        User userResponse = jwtTokenAuthService.findByEmail(email);
        Assertions.assertEquals(email, userResponse.getEmail());

        Mockito.verify(userRepository).findByEmail(email);
        
    }

    @Test
    void testFindByEmailNotFound() {
        String email = "kaguyachan@gmail.com";

        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            jwtTokenAuthService.findByEmail(email);
        });

        Mockito.verify(userRepository).findByEmail(email);

    }
}