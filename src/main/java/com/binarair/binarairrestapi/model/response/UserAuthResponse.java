package com.binarair.binarairrestapi.model.response;

import com.binarair.binarairrestapi.model.entity.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

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
