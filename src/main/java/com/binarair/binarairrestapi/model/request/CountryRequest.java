package com.binarair.binarairrestapi.model.request;


import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryRequest {

    @NotEmpty(message = "Country code id is required.")
    private String countryCodeId;

    @NotEmpty(message = "Country name is required.")
    private String name;

}
