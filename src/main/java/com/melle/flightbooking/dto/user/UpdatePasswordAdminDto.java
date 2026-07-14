package com.melle.flightbooking.dto.user;

import static com.melle.flightbooking.config.ValidationConstants.* ;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdatePasswordAdminDto {
    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer id;

    @NotBlank(message = PASSWORD_BLANK_MESSAGE)
    @Size(min = PASSWORD_MIN, max = PASSWORD_MAX, message = PASSWORD_SIZE_MESSAGE)
    @Pattern(
            regexp = PASSWORD_REGEXP,
            message = PASSWORD_REGEXP_MESSAGE
    )
    private String password;

    public UpdatePasswordAdminDto(Integer id, String password) {
        this.id = id;
        this.password = password;
    }

}