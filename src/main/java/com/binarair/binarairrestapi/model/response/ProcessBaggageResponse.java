package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessBaggageResponse {

    private String id;

    private Integer extraBagage;

    private Integer weight;

    private BigDecimal price;

    private Integer freeBagageCapacity;

    private Integer freeCabinCapacity;

    private String aircraftModel;

    private String aircraftManufacture;

    private LocalDateTime createdAt;
}
