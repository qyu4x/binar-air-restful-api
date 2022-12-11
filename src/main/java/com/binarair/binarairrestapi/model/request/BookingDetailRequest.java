package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDetailRequest {

    private BigDecimal amount;

    @NotEmpty(message = "booking type required.")
    private String bookingType;

    @NotNull(message = "number of adult passengers is required.")
    @Min(value = 0L, message = "adult passenger be positive")
    private Integer adult;

    @NotNull(message = "number of child passengers is required.")
    @Min(value = 0L, message = "child passenger be positive")
    private Integer child;

    @NotNull(message = "number of infant passengers is required.")
    @Min(value = 0L, message = "infant passenger be positive")
    private Integer infant;

    private BookingRequest departures;

    private BookingRequest returns;

}
