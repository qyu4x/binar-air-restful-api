package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, String> {
}
