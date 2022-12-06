package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineRequest {

    @NotEmpty(message = "airline name is required.")
    private String airlineName;

    @NotEmpty(message = "description is required.")
    private String description;

}
