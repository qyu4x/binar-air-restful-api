package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityRequest {

    @NotEmpty(message = "The city code id is required.")
    private String cityId;

    @NotEmpty(message = "The name is required.")
    private String name;

    @NotEmpty(message = "The country code is required.")
    private String countryCodeId;

}
