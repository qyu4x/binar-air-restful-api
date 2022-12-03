package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirportRequest {

    @NotEmpty(message = "iata code id is required.")
    private String iata;

    @NotEmpty(message = "name id is required.")
    private String name;

    @NotEmpty(message = "city code id is required.")
    private String cityCode;

}
