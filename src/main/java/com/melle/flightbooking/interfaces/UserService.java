package com.melle.flightbooking.interfaces;

import com.melle.flightbooking.dto.LoginResponseDto;
import com.melle.flightbooking.dto.UserSummaryDto;
import com.melle.flightbooking.model.RoleEnum;
import com.melle.flightbooking.model.User;

import java.util.Optional;

public interface UserService {
    UserSummaryDto register(User user);
    Boolean deleteUserById(Integer id);
    LoginResponseDto login(String email, String password);
    Optional<User> findByEmail(String email);
    Iterable<UserSummaryDto> getAllUsers();
    UserSummaryDto updateUsernameById(Integer id, String username);
    UserSummaryDto updateEmailById(Integer id, String email);
    UserSummaryDto updatePasswordById(Integer id, String password);
    UserSummaryDto updateRoleById(Integer id, RoleEnum role);
}
