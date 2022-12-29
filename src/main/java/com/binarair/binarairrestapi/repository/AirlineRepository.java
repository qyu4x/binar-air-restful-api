package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Airlines;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airlines, String> {
}
