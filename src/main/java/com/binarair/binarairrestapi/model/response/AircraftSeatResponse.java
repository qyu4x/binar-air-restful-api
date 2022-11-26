package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftSeatResponse {

    private String seatId;

    private String seatCode;

    private String aircraftId;

    private String aircraftManufacture;

    private String aircraftModel;

    private Boolean status;

    private PriceResponse price;

    private LocalDateTime createdAt;

    private LocalDateTime upatedAt;

}
