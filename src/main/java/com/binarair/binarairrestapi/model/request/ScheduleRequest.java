package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleRequest {

    @NotEmpty(message = "Origin iata airport code is required.")
    private String originIataAirportCode;

    @NotEmpty(message = "Destination iata airport code is required.")
    private String destinationIataAirportCode;

    @NotEmpty(message = "Aircraft id is required.")
    private String aircraftId;

    @FutureOrPresent(message = "Must be current or future date.")
    private LocalDate departureDate;

    @FutureOrPresent(message = "Must be current or future date.")
    private LocalDate arrivalDate;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    @Min(value = 0)
    @DecimalMax(value = "9999999999.999", message = "The decimal value can not be more than 9999999999.999")
    private BigDecimal price;

    @Min(value = 0L, message = "Stock must be positive")
    private Integer stock;


}
