package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketJasperReportRequest {
    @NotEmpty(message = "last name is required.")
    private String lastName;
    @NotEmpty(message = "Title is required.")
    private String title;
    @NotEmpty(message = "booking reference number is required.")
    private String bookingReferenceNumber;
}
