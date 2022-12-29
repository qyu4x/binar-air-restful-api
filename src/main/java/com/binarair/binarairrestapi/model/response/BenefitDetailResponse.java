package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BenefitDetailResponse {

    private String id;

    private String name;

    private String desription;

    private Boolean status;

    private String aircraftManufacture;

    private String aircraftModel;

    private LocalDateTime createdAt;

}
