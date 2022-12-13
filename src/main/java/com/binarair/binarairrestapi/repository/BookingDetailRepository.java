package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, String> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM booking_detail bd\n" +
                    "INNER JOIN passenger p ON bd.passenger_unique_id = p.id\n" +
                    "INNER JOIN schedule s ON bd.schedule_unique_id = s.id\n" +
                    "WHERE bd.booking_reference_number = :bookingReferenceNumber AND bd.check_in_status = :checkInStatus AND p.last_name = :lastName"
    )
    BookingDetail findCheckInBookingDetail(@Param("bookingReferenceNumber") String bookingReferenceNumber,
                              @Param("checkInStatus") boolean checkInStatus,
                              @Param("lastName") String lastName);
}
