package com.binarair.binarairrestapi.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftSeatBookingResponse {

    private String seatCode;

    private PriceResponse price;

}
