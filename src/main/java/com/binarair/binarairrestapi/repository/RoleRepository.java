package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Role;
import com.binarair.binarairrestapi.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleType> {
}
