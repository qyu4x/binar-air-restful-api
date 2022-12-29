package com.binarair.binarairrestapi.model.response;

import com.binarair.binarairrestapi.model.entity.PromoBanner;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PromoBannerPaggableResponse {

    public PromoBannerPaggableResponse(PromoBanner promoBanner) {
        this.id = promoBanner.getId();
        this.title = promoBanner.getTitle();
        this.description = promoBanner.getDescription();
        this.imageURL = promoBanner.getImageURL();
        this.createdAt = promoBanner.getCreatedAt();
    }

    private String id;

    private String title;

    private String description;

    private String imageURL;

    private LocalDateTime createdAt;

}
