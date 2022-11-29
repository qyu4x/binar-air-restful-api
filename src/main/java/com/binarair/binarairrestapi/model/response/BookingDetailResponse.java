package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDetailResponse {

    private List<PassengerBookingResponse> data;
}
