package com.binarair.binarairrestapi.model.response;

import com.binarair.binarairrestapi.model.entity.Facility;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {

    private String id;

    private String airLinesLogoURL;

    private String airlines;

    private String flightClass;

    private String iataOriginAirport;

    private String originAirport;

    private String originCity;

    private String iataDestinationAirport;

    private String destinationAirport;

    private String destinationCity;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private String flightDuration;

    private Integer stock;

    private Integer sold;

    private Integer capacity;

    private PriceResponse price;

    private AircraftResponse aircraft;

    private List<FacilityResponse> facilities;

    private List<BenefitResponse> benefits;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;






}
