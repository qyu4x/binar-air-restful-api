package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BaggageBookingResponse {
    private BigDecimal bagagePricePer5kg;

    private Integer freeBagageCapacity;

    private Integer freeCabinCapacity;

    private PriceResponse price;
}
