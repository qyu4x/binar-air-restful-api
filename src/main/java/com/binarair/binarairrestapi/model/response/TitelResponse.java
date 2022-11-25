package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TitelResponse {

    private String id;

    private String titelName;

    private String description;

    private LocalDateTime createdAt;

}
