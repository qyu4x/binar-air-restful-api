package com.binarair.binarairrestapi.model.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDetailRequest {

    private BigDecimal amount;

    private String bookingType;

    private BookingRequest departures;

    private BookingRequest returns;

}
