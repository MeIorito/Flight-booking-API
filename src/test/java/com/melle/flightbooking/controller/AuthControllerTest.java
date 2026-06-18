package com.melle.flightbooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melle.flightbooking.config.JwtFilter;
import com.melle.flightbooking.config.JwtUtil;
import com.melle.flightbooking.config.SecurityConfig;
import com.melle.flightbooking.dto.auth.LoginRequestDto;
import com.melle.flightbooking.dto.auth.LoginResponseDto;
import com.melle.flightbooking.dto.auth.RegisterRequestDto;
import com.melle.flightbooking.dto.user.UserSummaryDto;
import com.melle.flightbooking.exception.EmailDoesNotExistException;
import com.melle.flightbooking.exception.InvalidCredentialsException;
import com.melle.flightbooking.interfaces.UserService;
import com.melle.flightbooking.model.RoleEnum;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_ShouldReturnUserSummary_WhenRequestIsValid() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto("John", "john@example.com", "Password123");
        UserSummaryDto responseDto = new UserSummaryDto(1, "John", "john@example.com", RoleEnum.USER);

        Mockito.when(userService.register(any(RegisterRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void login_ShouldReturnLoginResponse_WhenRequestIsValid() throws Exception {
        UserSummaryDto user = new UserSummaryDto(1, "John", "john@example.com", RoleEnum.USER);
        LoginRequestDto request = new LoginRequestDto("John", "Password123");
        LoginResponseDto response = new LoginResponseDto(user, "mocked-jwt-token-string");

        Mockito.when(userService.login(any(LoginRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").value("mocked-jwt-token-string"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.username").value("John"))
                .andExpect(jsonPath("$.user.email").value("john@example.com"))
                .andExpect(jsonPath("$.user.role").value("USER"));
    }

    @Test
    void login_ShouldReturnError_WhenEmailDoesNotExist() throws Exception {
        LoginRequestDto request = new LoginRequestDto("unknown@example.com", "password123");

        Mockito.when(userService.login(any(LoginRequestDto.class)))
                .thenThrow(new EmailDoesNotExistException("Email does not exist"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void login_ShouldReturn401Unauthorized_WhenPasswordIsIncorrect() throws Exception {
        LoginRequestDto request = new LoginRequestDto("john@example.com", "Wrongpassword123");

        Mockito.when(userService.login(any(LoginRequestDto.class)))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
