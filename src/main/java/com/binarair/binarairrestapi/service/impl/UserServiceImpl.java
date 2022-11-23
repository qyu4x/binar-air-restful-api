package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.config.PasswordEncoderConfiguration;
import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Role;
import com.binarair.binarairrestapi.model.entity.User;
import com.binarair.binarairrestapi.model.enums.RoleType;
import com.binarair.binarairrestapi.model.request.UserRegisterRequest;
import com.binarair.binarairrestapi.model.response.UserRegisterResponse;
import com.binarair.binarairrestapi.repository.RoleRepository;
import com.binarair.binarairrestapi.repository.UserRepository;
import com.binarair.binarairrestapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoderConfiguration passwordEncoderConfiguration;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoderConfiguration passwordEncoderConfiguration) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
    }

    @Override
    public UserRegisterResponse save(UserRegisterRequest userRegisterRequest) {
        Role buyerRole = roleRepository.findById(RoleType.BUYER)
                .orElseThrow(() -> new DataNotFoundException("Role buyers are not available"));
        Optional<User> isUserExist = userRepository.findByEmail(userRegisterRequest.getEmail());
        if (!isUserExist.isEmpty()) {
           throw new DataAlreadyExistException(String.format("User with email %s already available", userRegisterRequest.getEmail()));
        }

        User user = User.builder()
                .id(String.format("ur-%s", UUID.randomUUID().toString()))
                .fullName(userRegisterRequest.getFullName())
                .email(userRegisterRequest.getEmail())
                .password(passwordEncoderConfiguration.passwordEncoder().encode(userRegisterRequest.getPassword()))
                .role(buyerRole)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        log.info("Successfully registered user with name {} ", userRegisterRequest.getFullName());
        return UserRegisterResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(buyerRole.getRole().name())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
