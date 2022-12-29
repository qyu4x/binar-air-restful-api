package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryResponse {

    private String bookingId;

    private String bookingType;

    private Integer adult;

    private Integer child;

    private Integer infant;

    private PriceResponse totalAmount;

    private BookingDetailResponse departure;

    private BookingDetailResponse returns;

    private LocalDateTime orderedAt;


}
