package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String id;

    private String fullName;

    private String email;

    private String role;

    private String gender;

    private LocalDate birthdate;

    private String imageURL;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
