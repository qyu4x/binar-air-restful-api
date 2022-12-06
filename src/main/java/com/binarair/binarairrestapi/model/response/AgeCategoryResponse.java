package com.binarair.binarairrestapi.model.response;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgeCategoryResponse {

    private String id;

    private String categoryName;

    private String description;

    private LocalDateTime createdAt;

}
