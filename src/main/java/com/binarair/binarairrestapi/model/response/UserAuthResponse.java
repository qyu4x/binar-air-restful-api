package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthResponse {

    private String id;

    private String email;

    private String fullName;

    private String role;

    private String tokenType;

    private String jwtToken;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
