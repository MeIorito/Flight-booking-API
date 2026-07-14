package com.melle.flightbooking.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import static com.melle.flightbooking.config.ValidationConstants.EMAIL_BLANK_MESSAGE;
import static com.melle.flightbooking.config.ValidationConstants.EMAIL_VALID_MESSAGE;

@Setter
@Getter
public class UpdateEmailDto {
    @NotBlank(message = EMAIL_BLANK_MESSAGE)
    @Email(message = EMAIL_VALID_MESSAGE)
    private String email;

    public UpdateEmailDto(String email) {
        this.email = email;
    }
}