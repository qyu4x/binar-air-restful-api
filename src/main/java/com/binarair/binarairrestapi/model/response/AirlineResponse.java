package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineResponse {

    private String id;

    private String airlineName;

    private String description;

    private String logoURL;

    private LocalDateTime createdAt;

}
