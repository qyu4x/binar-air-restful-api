package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftResponse {

    private String id;

    private String type;

    private String seatArrangement;

    private Integer distanceBetweenSeats;

    private String seatLengthUnit;

    private Integer freeBaggage;

    private Integer freeBaggageCabin;

}
