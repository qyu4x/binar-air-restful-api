package com.binarair.binarairrestapi.model.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingPassengerRequest {

    private String scheduleId;

    private String titelId;

    private String ageCategoryId;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String passportNumber;

    private  String issuingCountryId;

    private String citizenshipId;

    private BookingAircraftSeatRequest aircraftSeat;

    private BookingBaggageRequest baggage;

    private String status;

}
