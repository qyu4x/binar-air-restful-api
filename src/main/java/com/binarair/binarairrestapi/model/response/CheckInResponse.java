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
public class CheckInResponse {

    private String id;

    private String cityOrigin;

    private String cityDestination;

    private String iataOriginAirport;

    private String iataDestinationAirport;

    private String airportOrigin;

    private String destinationAirport;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    private String firstName;

    private String lastName;

    private String titel;

    private String bookingReferenceNumber;

    private boolean checkInStatus;

    private Integer baggageAllowance;

    private Integer extraBaggage;

    private String seatCode;

    private String travelClass;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
