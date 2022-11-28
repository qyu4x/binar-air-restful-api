package com.binarair.binarairrestapi.model.response;

import com.binarair.binarairrestapi.model.entity.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessPassengerResponse {

    private String id;

    private String status;

    private String scheduleId;

    private User user;

    private Titel titel;

    private AgeCategory ageCategory;

    private BookingDetail bookingDetail;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String passportNumber;

    private Country cityzenship;

    private Country issuingCountry;

    private AircraftSeatResponse seatResponse;

    private ProcessBaggageResponse baggageResponse;

    private LocalDateTime createdAt;


}
