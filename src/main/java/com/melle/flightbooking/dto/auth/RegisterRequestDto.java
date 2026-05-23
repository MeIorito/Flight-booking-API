package com.melle.flightbooking.dto.auth;

import static com.melle.flightbooking.config.ValidationConstants.* ;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequestDto {
    @NotBlank(message = USERNAME_BLANK_MESSAGE)
    @Size(min = USERNAME_MIN, max = USERNAME_MAX, message = USERNAME_SIZE_MESSAGE)
    @Pattern(
            regexp = USERNAME_REGEXP,
            message = USERNAME_REGEXP_MESSAGE
    )
    private String username;

    @NotBlank(message = EMAIL_BLANK_MESSAGE)
    @Email(message = EMAIL_VALID_MESSAGE)
    private String email;

    @NotBlank(message = PASSWORD_BLANK_MESSAGE)
    @Size(min = PASSWORD_MIN, max = PASSWORD_MAX, message = PASSWORD_SIZE_MESSAGE)
    @Pattern(
            regexp = PASSWORD_REGEXP,
            message = PASSWORD_REGEXP_MESSAGE
    )
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
