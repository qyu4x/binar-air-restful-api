package com.binarair.binarairrestapi.model.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDetailRequest {
    private BigDecimal amount;

    private String BookingType;

    private BookingRequest departures;

    private BookingRequest returns;
}
