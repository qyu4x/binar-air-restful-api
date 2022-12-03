package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface BookingRepository extends JpaRepository<Booking, String > {
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM booking bk\n" +
                    "JOIN booking_detail bd on bk.id = bd.booking_unique_id\n" +
                    "JOIN passenger p on bd.passenger_unique_id = p.id\n" +
                    "WHERE bk.id = :bookingid"
    )
    Booking findBookingDetailsById(@Param("bookingid") String bookingId);

}
