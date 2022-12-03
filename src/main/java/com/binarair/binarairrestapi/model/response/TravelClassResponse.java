package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelClassResponse {

    private String travelClassId;

    private String travelClassName;

    private LocalDateTime createdAt;
}
