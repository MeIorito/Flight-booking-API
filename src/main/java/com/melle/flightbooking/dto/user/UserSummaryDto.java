package com.melle.flightbooking.dto.user;

import com.melle.flightbooking.model.RoleEnum;

public record UserSummaryDto(Integer id, String username, String email, RoleEnum role) {}
