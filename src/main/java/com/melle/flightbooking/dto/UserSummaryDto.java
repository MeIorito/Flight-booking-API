package com.melle.flightbooking.dto;

import com.melle.flightbooking.model.RoleEnum;

public record UserSummaryDto(Integer id, String username, String email, RoleEnum role) {}
