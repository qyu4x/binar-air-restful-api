package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse {

    private String id;

    private String imageURL;

    private String fullName;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
