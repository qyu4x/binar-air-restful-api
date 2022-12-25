package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingPassengerRequest {

    @NotEmpty(message = "schedule id is required.")
    private String scheduleId;

    @NotEmpty(message = "titel id is required.")
    private String titelId;

    @NotEmpty(message = "age category id is required.")
    private String ageCategoryId;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String passportNumber;

    @NotEmpty(message = "issuing country is required.")
    private  String issuingCountryId;

    @NotEmpty(message = "citizenship id is required.")
    private String citizenshipId;

    private BookingAircraftSeatRequest aircraftSeat;

    private BookingBaggageRequest baggage;

    private String status;

}
