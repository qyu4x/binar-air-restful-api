package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.AircraftManufacture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AircraftManufactureRepository extends JpaRepository<AircraftManufacture, String> {

    Optional<AircraftManufacture> findByName(String name);

}
