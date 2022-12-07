package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketJasperRequest {

    @NotEmpty(message = "last name is required.")
    private String lastName;

    @NotEmpty(message = "booking reference number is required.")
    private String bookingReferenceNumber;
}
