package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, String> {
}
