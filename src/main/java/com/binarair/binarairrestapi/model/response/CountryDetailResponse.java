package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CountryDetailResponse {
    private String countryCodeId;

    private String name;

    List<CityResponse> cityResponses;

    private LocalDateTime createdAt;

}
