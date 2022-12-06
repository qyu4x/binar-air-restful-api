package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.AircraftSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AircraftSeatRepository extends JpaRepository<AircraftSeat, String> {

    @Query(value = "SELECT * FROM aircraft_seat ai\n" +
            "LEFT JOIN seat_schedule_booking ssb on (ai.id = ssb.unique_aircraft_seat_id)\n" +
            "WHERE ssb.id IS NULL AND ai.aircraft_unique_id =:id",
            nativeQuery = true
    )
    List<AircraftSeat> findAllAvailableSeatByAircraftId(@Param("id") String id);

    @Query(value = "SELECT * FROM aircraft_seat ai\n" +
            "LEFT JOIN seat_schedule_booking ssb on (ai.id = ssb.unique_aircraft_seat_id)\n" +
            "WHERE ssb.id IS NOT NULL AND ai.aircraft_unique_id =:id",
            nativeQuery = true)
    List<AircraftSeat> findAllReservedSeatByAircraftId(@Param("id") String id);

    @Query(value = "SELECT * FROM aircraft_seat ai\n" +
            "LEFT JOIN seat_schedule_booking ssb on (ai.id = ssb.unique_aircraft_seat_id)",
            nativeQuery = true)
    List<AircraftSeat> findAllByAircraftId(@Param("id") String id);

    @Query(value = "SELECT * FROM aircraft_seat WHERE aircraft_unique_id = :aircraftId AND seat_code = :seatCode",
            nativeQuery = true)
    AircraftSeat findByAircraftAndSeatCode(@Param("aircraftId")String aircraftId, @Param("seatCode")String seatCode);

}
