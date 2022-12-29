package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftSeatRequest {

    @NotEmpty(message = "aircraft id is required.")
    private String aircraftId;

    @NotEmpty(message = "seat code is required.")
    private String seatCode;

    @DecimalMax(value = "9999999999.999", message = "The decimal value can not be more than 9999999999.999")
    private BigDecimal price;

}
