package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

    @NotEmpty(message = "Full name is required.")
    private String fullName;

    @Past(message = "Birth date is required.")
    private LocalDate birthDate;

    @NotEmpty(message = "Gender is required.")
    private String gender;

    @NotEmpty(message = "City code is required.")
    private String cityId;

}
