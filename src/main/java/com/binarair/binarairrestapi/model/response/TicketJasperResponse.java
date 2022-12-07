package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketJasperResponse {

    private String id;
    private String titel;
    private String firstName;
    private String lastName;
    private String dateOfFlight;
    private String classType;
    private String boardingTime;
    private String cityOrigin;
    private String seatCode;
    private String cityDestination;

    private LocalDate departureDate;

    private LocalTime departureTime;

    private BookingDetailResponse departure;

    private String bookingReferenceNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
