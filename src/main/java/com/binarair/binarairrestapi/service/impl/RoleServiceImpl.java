package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.model.entity.Role;
import com.binarair.binarairrestapi.model.enums.RoleType;
import com.binarair.binarairrestapi.repository.RoleRepository;
import com.binarair.binarairrestapi.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
public class RoleServiceImpl implements RoleService {

    private final static Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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
}
