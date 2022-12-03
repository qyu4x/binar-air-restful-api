package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Bagage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BagageRepository extends JpaRepository<Bagage,String> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM bagage WHERE aircraft_unique_id = :aircraftId")
    Bagage findByAircraftId(@Param("aircraftId") String aircraftId);

}
