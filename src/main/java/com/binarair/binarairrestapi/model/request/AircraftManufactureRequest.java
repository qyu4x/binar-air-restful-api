package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftManufactureRequest {

    @NotEmpty(message = "name is required.")
    private String name;

}
