package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelClassUpdateRequest {

    @NotEmpty(message = "Travel class name is required")
    @NotEmpty(message = "Travel class name is required")
    private String travelClassName;

}
