package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.config.PasswordEncoderConfiguration;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Role;
import com.binarair.binarairrestapi.model.entity.User;
import com.binarair.binarairrestapi.model.enums.RoleType;
import com.binarair.binarairrestapi.repository.RoleRepository;
import com.binarair.binarairrestapi.repository.UserRepository;
import com.binarair.binarairrestapi.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final static Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoderConfiguration passwordEncoderConfiguration;


    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoderConfiguration passwordEncoderConfiguration) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
    }

    @Override
    @PostConstruct
    public void initRole() {
        boolean isAdminExist = roleRepository.existsById(RoleType.ADMIN);
        boolean isBuyerExist = roleRepository.existsById(RoleType.BUYER);
        if (!isAdminExist && !isBuyerExist) {
            saveInitAdmin();
            saveInitBuyer();
            log.info("Successfully entered admin and buyer roles");
        } else {
            log.warn("Buyer and Admin roles are available");
        }

        Optional<User> adminRole = userRepository.findByRole(RoleType.ADMIN.name());
        if(!adminRole.isPresent()) {
            saveInitDataAdmin();
            log.info("Successfully enter admin data");
        }

    }

    private void saveInitBuyer() {
        Role buyerRole = new Role();
        buyerRole.setRole(RoleType.BUYER);
        buyerRole.setCreatedAt(LocalDateTime.now());
        roleRepository.save(buyerRole);
    }

    private void saveInitAdmin() {
        Role adminRole = new Role();
        adminRole.setRole(RoleType.ADMIN);
        adminRole.setCreatedAt(LocalDateTime.now());
        roleRepository.save(adminRole);
    }

    private void saveInitDataAdmin() {
        Role adminRole = roleRepository.findById(RoleType.ADMIN)
                .orElseThrow(() -> new DataNotFoundException("Role buyers are not available"));

        User admin1 = User.builder()
                .id(String.format("ad-%s", UUID.randomUUID().toString()))
                .fullName("Binar Air")
                .email("binarair@gmail.com")
                .password(passwordEncoderConfiguration.passwordEncoder().encode("kelompok3"))
                .role(adminRole)
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();
        userRepository.save(admin1);
    }
}
