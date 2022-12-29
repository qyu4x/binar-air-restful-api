package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Benefit;
import com.binarair.binarairrestapi.model.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BenefitRepository extends JpaRepository<Benefit, String> {
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM benefit WHERE aircraft_unique_id = :id"
    )
    List<Benefit> findBenefitByAircraftId(@Param("id") String id);
}
