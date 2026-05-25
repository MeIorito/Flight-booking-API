package com.melle.flightbooking.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.melle.flightbooking.config.ValidationConstants.* ;

public class UpdateUsernameAdminDto {
    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer id;

    @NotBlank(message = USERNAME_BLANK_MESSAGE)
    @Size(min = USERNAME_MIN, max = USERNAME_MAX, message = USERNAME_SIZE_MESSAGE)
    @Pattern(
            regexp = USERNAME_REGEXP,
            message = USERNAME_REGEXP_MESSAGE
    )
    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}