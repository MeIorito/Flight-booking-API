package com.melle.flightbooking.service;

import com.melle.flightbooking.config.JwtUtil;
import com.melle.flightbooking.dto.auth.LoginRequestDto;
import com.melle.flightbooking.dto.auth.LoginResponseDto;
import com.melle.flightbooking.dto.auth.RegisterRequestDto;
import com.melle.flightbooking.dto.user.UserSummaryDto;
import com.melle.flightbooking.exception.EmailAlreadyExistsException;
import com.melle.flightbooking.model.RoleEnum;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImp userService;

    @Test
    void register_whenEmailNotInUse_returnsUserSummaryDto() {
        RegisterRequestDto request = new RegisterRequestDto("test", "user@test.com", "Password123");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("test");
        savedUser.setEmail("user@test.com");
        savedUser.setRole(RoleEnum.USER);

        when(userRepository.existsByEmail("user@test.com")).thenReturn(false);
        when(passwordEncoder.encode("Password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserSummaryDto result = userService.register(request);

        assertNotNull(result);
        assertEquals("test", result.username());
        assertEquals("user@test.com", result.email());
        assertEquals(RoleEnum.USER, result.role());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_whenEmailInUse_throwsEmailAlreadyExistsException() {
        RegisterRequestDto request = new RegisterRequestDto("test", "user@test.com", "Password123");

        when(userRepository.existsByEmail("user@test.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> userService.register(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_whenCredentialsAreValid_returnsLoginResponseDto() {
        LoginRequestDto request = new LoginRequestDto("user@test.com", "password123");

        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setEmail("user@test.com");
        user.setPassword("hashedPassword");
        user.setRole(RoleEnum.USER);

        when(userRepository.existsByEmail("user@test.com")).thenReturn(true);
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(user);
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtUtil.createToken(any(), eq("user@test.com"))).thenReturn("jwt-token");

        LoginResponseDto result = userService.login(request);

        assertNotNull(result);
        assertEquals("jwt-token", result.getJwtToken());
        assertEquals("user@test.com", result.getUser().email());
        verify(jwtUtil).createToken(any(), eq("user@test.com"));
    }
}