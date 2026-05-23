package com.melle.flightbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.melle.flightbooking.config.ValidationConstants.*;
import static com.melle.flightbooking.config.ValidationConstants.USERNAME_REGEXP;
import static com.melle.flightbooking.config.ValidationConstants.USERNAME_REGEXP_MESSAGE;
import static com.melle.flightbooking.config.ValidationConstants.USERNAME_SIZE_MESSAGE;

public class UpdateUsernameDto {
    @NotBlank(message = USERNAME_BLANK_MESSAGE)
    @Size(min = USERNAME_MIN, max = USERNAME_MAX, message = USERNAME_SIZE_MESSAGE)
    @Pattern(
            regexp = USERNAME_REGEXP,
            message = USERNAME_REGEXP_MESSAGE
    )
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}