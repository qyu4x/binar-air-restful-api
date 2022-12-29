package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingPassengerRequest {

    @NotNull(message = "schedule id is required.")
    @NotEmpty(message = "schedule id is required.")
    private String scheduleId;

    @NotNull(message =  "titel id is required.")
    @NotEmpty(message = "titel id is required.")
    private String titelId;

    @NotNull(message = "age category id is required.")
    @NotEmpty(message = "age category id is required.")
    private String ageCategoryId;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String passportNumber;

    @NotNull(message = "issuing country is required.")
    @NotEmpty(message = "issuing country is required.")
    private  String issuingCountryId;

    @NotNull(message = "citizenship id is required.")
    @NotEmpty(message = "citizenship id is required.")
    private String citizenshipId;

    private BookingAircraftSeatRequest aircraftSeat;

    private BookingBaggageRequest baggage;

    private String status;

}
