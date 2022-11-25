package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateResponse {

    private String id;

    private String fullName;

    private String email;

    private String role;

    private String gender;

    private LocalDate birthdate;

    private String cityCode;

    private String city;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
