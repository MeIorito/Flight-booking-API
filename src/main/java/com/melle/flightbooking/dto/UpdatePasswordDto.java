package com.melle.flightbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.melle.flightbooking.config.ValidationConstants.*;
import static com.melle.flightbooking.config.ValidationConstants.PASSWORD_REGEXP;
import static com.melle.flightbooking.config.ValidationConstants.PASSWORD_REGEXP_MESSAGE;
import static com.melle.flightbooking.config.ValidationConstants.PASSWORD_SIZE_MESSAGE;

public class UpdatePasswordDto {
    @NotBlank(message = PASSWORD_BLANK_MESSAGE)
    @Size(min = PASSWORD_MIN, max = PASSWORD_MAX, message = PASSWORD_SIZE_MESSAGE)
    @Pattern(
            regexp = PASSWORD_REGEXP,
            message = PASSWORD_REGEXP_MESSAGE
    )
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}