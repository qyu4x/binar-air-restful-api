package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromoBannerResponse {

    private String id;

    private String title;

    private String description;

    private String imageURL;

    private LocalDateTime createdAt;

}
