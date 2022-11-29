package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.SeatScheduleBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatScheduleBookingRepository extends JpaRepository<SeatScheduleBooking, String> {

    SeatScheduleBooking findSeatScheduleBookingByAircraftSeat(String id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM seat_schedule_booking ssb\n" +
                    "WHERE ssb.unique_schedule_id = :scheduleId AND ssb.unique_aircraft_seat_id = :uniqueSeatId"
    )
    SeatScheduleBooking checkSeatStatus(@Param("scheduleId") String scheduleId,
                                        @Param("uniqueSeatId") String uniqueSeatId);

}
