package com.melle.flightbooking.interfaces;

import com.melle.flightbooking.dto.auth.LoginResponseDto;
import com.melle.flightbooking.dto.auth.RegisterRequestDto;
import com.melle.flightbooking.dto.user.UserSummaryDto;
import com.melle.flightbooking.model.RoleEnum;
import com.melle.flightbooking.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    UserSummaryDto register(RegisterRequestDto user);
    void deleteUserById(Integer id);
    LoginResponseDto login(String email, String password);
    Optional<User> findByEmail(String email);
    UserSummaryDto updateUsernameById(Integer id, String username);
    UserSummaryDto updateEmailById(Integer id, String email);
    UserSummaryDto updatePasswordById(Integer id, String password);
    UserSummaryDto updateRoleById(Integer id, RoleEnum role);
    Page<UserSummaryDto> getAllUsers(Pageable pageable);
    Page<UserSummaryDto> getUsersByFilters(String username, String email, RoleEnum role, Pageable pageable);
}
