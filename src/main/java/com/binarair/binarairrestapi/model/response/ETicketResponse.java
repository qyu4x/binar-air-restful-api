package com.binarair.binarairrestapi.model.response;

import com.binarair.binarairrestapi.model.entity.AgeCategory;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ETicketResponse {

    private String id;
    private String titel;
    private String fullName;
    private AgeCategory ageCategory;
    private String baggage;
    private String dateOfFlight;
    private String FromCity;
    private String DestinationCity;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private BookingDetailResponse departure;
    private String bookingReferenceNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}