package com.binarair.binarairrestapi.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftResponse {

    private String type;

    private String seatArrangement;

    private Integer distanceBetweenSeats;

    private String seatLengthUnit;

}
