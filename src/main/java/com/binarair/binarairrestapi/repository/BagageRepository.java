package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Bagage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BagageRepository extends JpaRepository<Bagage, String> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM bagage WHERE aircraft_unique_id = :aircraftId ORDER BY weight ASC")
    List<Bagage> findByAircraftId(@Param("aircraftId") String aircraftId);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM bagage WHERE aircraft_unique_id = :aircraftId AND weight = :weight")
    Bagage findByAircraftIdAndBaggageWeight(@Param("aircraftId") String aircraftId, @Param("weight") Integer weight);

}
