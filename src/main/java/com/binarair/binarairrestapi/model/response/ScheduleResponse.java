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
public class ScheduleResponse {

    private String id;

    private AirportResponse originAirport;

    private AirportResponse destinationAirport;

    private AircraftResponse aircraft;

    private PriceResponse price;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private Integer stock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
