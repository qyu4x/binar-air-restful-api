package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.AircraftSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AircraftSeatRepository extends JpaRepository<AircraftSeat, String> {

    @Query(value = "SELECT * FROM aircraft_seat ai\n" +
            "LEFT JOIN seat_schedule_booking ssb on (ai.id = ssb.unique_aircraft_seat_id)\n" +
            "WHERE ssb.id IS NULL AND ai.aircraft_unique_id =:id ORDER BY ai.price DESC, ai.seat_code ASC",
            nativeQuery = true
    )
    List<AircraftSeat> findAllAvailableSeatByAircraftId(@Param("id") String id);

    @Query(value = "SELECT * FROM aircraft_seat ai\n" +
            "LEFT JOIN seat_schedule_booking ssb on (ai.id = ssb.unique_aircraft_seat_id)\n" +
            "WHERE ssb.id IS NOT NULL AND ai.aircraft_unique_id =:id ORDER BY ai.price DESC, ai.seat_code ASC",
            nativeQuery = true)
    List<AircraftSeat> findAllReservedSeatByAircraftId(@Param("id") String id);

    @Query(value = "SELECT ai.id, ai.seat_code, ai.aircraft_unique_id, ai.created_at, ai.updated_at, ai.price\n" +
            "FROM aircraft_seat ai\n" +
            "    LEFT OUTER JOIN seat_schedule_booking ssb ON ai.id = ssb.unique_aircraft_seat_id\n" +
            "WHERE ai.aircraft_unique_id = :id\n" +
            "GROUP BY ai.seat_code, ai.id, ai.price\n" +
            "ORDER BY ai.price DESC, ai.seat_code ASC",
            nativeQuery = true)
    List<AircraftSeat> findAllByAircraftId(@Param("id") String id);

    @Query(value = "SELECT * FROM aircraft_seat WHERE aircraft_unique_id = :aircraftId AND seat_code = :seatCode",
            nativeQuery = true)
    AircraftSeat findByAircraftAndSeatCode(@Param("aircraftId")String aircraftId, @Param("seatCode")String seatCode);

}
