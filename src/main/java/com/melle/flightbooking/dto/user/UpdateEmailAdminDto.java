package com.melle.flightbooking.dto.user;

import static com.melle.flightbooking.config.ValidationConstants.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateEmailAdminDto {
    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer id;

    @NotBlank(message = EMAIL_BLANK_MESSAGE)
    @Email(message = EMAIL_VALID_MESSAGE)
    private String email;

    public UpdateEmailAdminDto(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

}