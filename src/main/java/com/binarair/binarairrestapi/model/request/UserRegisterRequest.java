package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequest {

    @NotEmpty(message = "Email address is required.")
    @Email(message = "The email address is invalid.", flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;

    @NotEmpty(message = "Full name is required.")
    @Size(min = 2, max = 100, message = "The length of name must be between 2 and 100 characters.")
    private String fullName;

    @NotEmpty(message = "Password is required.")
    private String password;

}
