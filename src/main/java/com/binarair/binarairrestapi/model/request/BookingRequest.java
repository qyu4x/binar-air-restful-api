package com.binarair.binarairrestapi.model.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {

    private List<BookingPassengerRequest> data;


}
