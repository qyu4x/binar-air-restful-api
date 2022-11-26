package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftDetailResponse {

    private String id;

    private String model;

    private Integer passengerCapacity;

    private Integer totalUnit;

    private String seatArrangement;

    private Integer distanceBetweenSeat;

    private String seatLengthUnit;

    private AircraftManufactureResponse manufacture;

    private TravelClassResponse travel;

    private AirlineResponse airlines;

    private LocalDateTime createdAt;

}
