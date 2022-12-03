package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CityResponse {

    private String cityId;

    private String cityName;

    private String countryCodeId;

    private String countryName;

    private LocalDateTime createdAt;
}
