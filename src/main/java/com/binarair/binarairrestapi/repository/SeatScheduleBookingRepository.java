package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.SeatScheduleBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatScheduleBookingRepository extends JpaRepository<SeatScheduleBooking, String> {

    SeatScheduleBooking findSeatScheduleBookingByAircraftSeat(String id);

}
