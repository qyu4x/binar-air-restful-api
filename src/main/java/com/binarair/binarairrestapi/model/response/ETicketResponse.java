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
    private String firstName;
    private String lastName;
    private String passportNumber;
    private AgeCategory ageCategory;
    private Integer baggage;
    private String fromCity;
    private String destinationCity;
    private LocalDate departureDate;
    private String departureAirport;
    private String arrivalAirport;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    private BookingDetailResponse departure;
    private String classType;
    private String bookingReferenceNumber;
    private String bookingId;
    private String aircraftType;
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}