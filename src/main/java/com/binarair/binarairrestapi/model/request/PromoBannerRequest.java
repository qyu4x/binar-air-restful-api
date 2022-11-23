package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromoBannerRequest {

    @NotEmpty(message = "Title is required.")
    private String title;

    @NotEmpty(message = "Desciption is required.")
    private String description;

}
