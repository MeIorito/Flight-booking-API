package com.melle.flightbooking.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static com.melle.flightbooking.config.ValidationConstants.* ;

@Setter
@Getter
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

    public UpdateUsernameAdminDto(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

}