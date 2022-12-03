package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenefitRequest {
    @NotEmpty(message = "aircraft id is required.")
    private String aircraftId;

    @NotEmpty(message = "name is required.")
    private String name;

    @NotEmpty(message = "description is required.")
    private String desription;

    @NotNull(message = "status is required." )
    private Boolean status;

}
