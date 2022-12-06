package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FacilityRepository extends JpaRepository<Facility, String> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM facility WHERE aircraft_unique_id = :id"
    )
    List<Facility> findFacilitiesByAircraftId(@Param("id") String id);

}
