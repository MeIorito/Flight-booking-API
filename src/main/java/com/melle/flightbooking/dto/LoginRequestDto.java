package com.melle.flightbooking.dto;

import jakarta.validation.constraints.*;

public class LoginRequestDto {

    @NotBlank(message = "Email can not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password can not be blank")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

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
