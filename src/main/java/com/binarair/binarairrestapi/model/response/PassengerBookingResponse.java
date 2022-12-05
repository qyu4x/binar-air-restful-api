package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerBookingResponse {

    private String passengerId;

    private String titel;

    private String ageCategory;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String citizenship;

    private String passportNumber;

    private String issuingCountry;

    private String bookingReferenceNumber;

    private boolean checkInStatus;

    private ScheduleResponse schedule;

    private AircraftSeatResponse aircraftSeat;

    private BaggageBookingResponse bagage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
