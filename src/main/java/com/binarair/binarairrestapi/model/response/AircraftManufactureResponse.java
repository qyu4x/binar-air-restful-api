package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftManufactureResponse {

    private String id;

    private String name;

    private LocalDateTime createdAt;

}
