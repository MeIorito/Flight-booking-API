package com.melle.flightbooking.dto;

public class CustomUserPrincipal {

    private Integer id;
    private String email;

    public CustomUserPrincipal(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

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
