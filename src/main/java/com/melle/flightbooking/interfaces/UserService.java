package com.melle.flightbooking.interfaces;

import com.melle.flightbooking.dto.LoginResponseDto;
import com.melle.flightbooking.dto.UserSummaryDto;
import com.melle.flightbooking.model.User;

import java.util.Optional;

public interface UserService {
    User register(User user);
    Boolean deleteUserById(Integer id);
    LoginResponseDto login(String email, String password);
    Optional<User> findByEmail(String email);
    Iterable<UserSummaryDto> getAllUsers();
}
