package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {

    private String id;

    private String title;

    private String description;

    private boolean isRead;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

