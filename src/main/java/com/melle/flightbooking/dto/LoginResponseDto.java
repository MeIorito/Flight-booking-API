package com.melle.flightbooking.dto;

import com.melle.flightbooking.model.User;

public class LoginResponseDto {
    private UserSummaryDto user;
    private String jwtToken;

    public LoginResponseDto(UserSummaryDto user, String jwtToken){
        this.user = user;
        this.jwtToken = jwtToken;
    }

    public UserSummaryDto getUser() {
        return user;
    }

    public void setUser(UserSummaryDto user) {
        this.user = user;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
