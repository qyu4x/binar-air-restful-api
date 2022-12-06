package com.binarair.binarairrestapi.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityResponse {

    private String name;

    private String description;

    private boolean status;


}
