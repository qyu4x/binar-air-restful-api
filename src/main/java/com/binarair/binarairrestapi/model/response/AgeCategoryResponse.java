package com.binarair.binarairrestapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NotEmpty
@Builder
public class AgeCategoryResponse {

    private String id;

    private String categoryName;

    private String description;

    private LocalDateTime createdAt;

}
