package com.melle.flightbooking.dto;

import com.melle.flightbooking.model.User;

public class LoginResponseDto {
    private User user;
    private String jwtToken;

    public LoginResponseDto(User user, String jwtToken){
        this.user = user;
        this.jwtToken = jwtToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
