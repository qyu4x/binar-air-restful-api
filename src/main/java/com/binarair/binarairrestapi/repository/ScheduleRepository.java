package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM schedule sc JOIN aircraft af on sc.aircraft_code_id = af.id JOIN travel_class tc on af.travel_class_code_id = tc.id WHERE sc.departure_date = DATE(:originDeparture) AND sc.origin_iata_airport_code_id = :originIataCode AND sc.dest_iata_airport_code_id = :destIataCode AND sc.stock != 0 AND af.travel_class_code_id = :flightClass"
    )
    List<Schedule> findFullTwoSearch(@Param("originDeparture") LocalDate originDeparture,
                                     @Param("originIataCode") String originIataCode,
                                     @Param("destIataCode") String destIataCode,
                                     @Param("flightClass") String flightClass);



}
