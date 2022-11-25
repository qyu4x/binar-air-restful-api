package com.binarair.binarairrestapi.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TitelRequest {

    private String titelName;

    private String description;
}
