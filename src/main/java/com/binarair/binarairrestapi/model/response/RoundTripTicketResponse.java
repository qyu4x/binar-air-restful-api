package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoundTripTicketResponse {

    List<TicketResponse> departures;

    List<TicketResponse> returns;


}
