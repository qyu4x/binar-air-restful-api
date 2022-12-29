package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryResponse {

    private String countryCodeId;

    private String name;

    private LocalDateTime createdAt;

}
