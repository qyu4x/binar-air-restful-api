package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgeCategoryRequest {

    @NotEmpty(message = "category name is required.")
    private String categoryName;

    @NotEmpty(message = "description is required.")
    private String description;

}
