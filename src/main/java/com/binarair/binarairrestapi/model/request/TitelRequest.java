package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TitelRequest {

    @NotEmpty(message = "Titel name is required.")
    private String titelName;

    @NotEmpty(message = "Description is required.")
    private String description;
}
