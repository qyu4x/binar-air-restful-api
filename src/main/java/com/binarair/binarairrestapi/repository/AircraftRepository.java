package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AircraftRepository extends JpaRepository<Aircraft, String> {

    Optional<Aircraft> findByModel(String model);

}
