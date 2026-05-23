package com.melle.flightbooking.dto;

import static com.melle.flightbooking.config.ValidationConstants.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Type;

public class UpdateEmailAdminDto {
    @NotBlank(message = ID_BLANK_MESSAGE)
    private Integer id;

    @NotBlank(message = EMAIL_BLANK_MESSAGE)
    @Email(message = EMAIL_VALID_MESSAGE)
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}