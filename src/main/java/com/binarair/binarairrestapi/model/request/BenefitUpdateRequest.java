package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BenefitUpdateRequest {

    @NotEmpty(message = "name of benefir is required.")
    private String name;

    @NotEmpty(message = "description is required.")
    private String description;

    @NotNull(message = "status is required." )
    private Boolean status;

}
