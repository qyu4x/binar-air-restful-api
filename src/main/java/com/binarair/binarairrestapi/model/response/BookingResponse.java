package com.binarair.binarairrestapi.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse {

    private String bookingId;

    private PriceResponse totalAmount;

    private BookingDetailResponse departure;

    private BookingDetailResponse returns;
}
