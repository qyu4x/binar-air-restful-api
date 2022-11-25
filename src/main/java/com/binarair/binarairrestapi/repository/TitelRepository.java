package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Titel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TitelRepository extends JpaRepository<Titel, String> {

    Optional<Titel> findByTitelName(String titelName);
}
