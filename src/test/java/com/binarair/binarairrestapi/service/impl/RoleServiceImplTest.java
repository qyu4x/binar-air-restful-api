package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.config.PasswordEncoderConfiguration;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Role;
import com.binarair.binarairrestapi.model.entity.User;
import com.binarair.binarairrestapi.model.enums.RoleType;
import com.binarair.binarairrestapi.repository.RoleRepository;
import com.binarair.binarairrestapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderConfiguration passwordEncoderConfiguration;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void testInitRoleIfBuyerAndAdminRoleNotAvailable() {
        Role buyerRole = new Role();
        buyerRole.setRole(RoleType.BUYER);

        Role adminRole = new Role();
        buyerRole.setRole(RoleType.BUYER);

        User admin = User.builder()
                .id(String.format("random-uuid"))
                .fullName("Binar Air")
                .email("binarair@gmail.com")
                .password("secret")
                .role(adminRole)
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        Mockito.when(roleRepository.existsById(RoleType.BUYER))
                .thenReturn(false);
        Mockito.when(roleRepository.existsById(RoleType.ADMIN))
                .thenReturn(false);

        Mockito.when(roleRepository.save(ArgumentMatchers.any(Role.class)))
                .thenReturn(adminRole);

        Mockito.when(passwordEncoderConfiguration.passwordEncoder())
                .thenReturn(new BCryptPasswordEncoder());

        Mockito.when(roleRepository.findById(RoleType.ADMIN))
                .thenReturn(Optional.of(adminRole));

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(admin);

        roleService.initRole();

        Mockito.verify(roleRepository).existsById(RoleType.BUYER);
        Mockito.verify(roleRepository).existsById(RoleType.ADMIN);
        Mockito.verify(passwordEncoderConfiguration).passwordEncoder();
        Mockito.verify(roleRepository, Mockito.times(2)).save(ArgumentMatchers.any(Role.class));
        Mockito.verify(roleRepository).findById(RoleType.ADMIN);
        Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));

    }

    @Test
    void testSaveInitRoleAdminRoleNotFound() {
        Role buyerRole = new Role();
        buyerRole.setRole(RoleType.BUYER);

        Mockito.when(roleRepository.existsById(RoleType.BUYER))
                        .thenReturn(true);
        Mockito.when(roleRepository.existsById(RoleType.ADMIN))
                .thenReturn(true);;

        Mockito.when(roleRepository.findById(RoleType.ADMIN))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            roleService.initRole();
        });

        Mockito.verify(roleRepository).existsById(RoleType.BUYER);
        Mockito.verify(roleRepository).existsById(RoleType.ADMIN);
        Mockito.verify(roleRepository).findById(RoleType.ADMIN);


    }
}