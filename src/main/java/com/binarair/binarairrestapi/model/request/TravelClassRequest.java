package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelClassRequest {

    @NotEmpty(message = "Travel class is required")
    private String travelClassId;

    @NotEmpty(message = "Travel class name is required")
    private String travelClassName;

}
