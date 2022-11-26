package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityRequest {

    @NotEmpty(message = "aircraft id is required.")
    private String aircraftId;

    @NotEmpty(message = "name is required.")
    private String name;

    @NotEmpty(message = "description is required.")
    private String desription;

    @NotNull(message = "status is required." )
    private Boolean status;

}
