package com.binarair.binarairrestapi.model.response;

import com.binarair.binarairrestapi.model.entity.City;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirportResponse {

    private String iata;

    private String name;

    private String cityCode;

    private String city;

    private String countryCode;

    private String country;

    private LocalDateTime createdAt;
}
