//package com.melle.flightbooking.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.melle.flightbooking.config.JwtFilter;
//import com.melle.flightbooking.config.JwtUtil;
//import com.melle.flightbooking.config.SecurityConfig;
//import com.melle.flightbooking.dto.CustomUserPrinciple;
//import com.melle.flightbooking.dto.user.*;
//import com.melle.flightbooking.interfaces.UserService;
//import com.melle.flightbooking.model.RoleEnum;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.*;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(
//        controllers = UserController.class,
//        excludeFilters = @ComponentScan.Filter(
//                type = FilterType.ASSIGNABLE_TYPE,
//                classes = { SecurityConfig.class, JwtFilter.class }
//        ),
//        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
//)
//@Import(UserControllerTest.TestSecurityConfig.class)
//class UserControllerTest {
//
//    @TestConfiguration
//    static class TestSecurityConfig {
//        @Bean
//        @Primary
//        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//            http
//                    .csrf(AbstractHttpConfigurer::disable)
//                    .anonymous(AbstractHttpConfigurer::disable)  // (1)
//                    .sessionManagement(session -> session
//                            .sessionCreationPolicy(
//                                    org.springframework.security.config.http.SessionCreationPolicy.STATELESS
//                            )                                    // (2)
//                    )
//                    .authorizeHttpRequests(auth -> auth
//                            .anyRequest().permitAll()
//                    );
//            return http.build();
//        }
//    }
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private UserService userService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private void setUserInSecurityContext(Integer id, String email, String role) {
//        CustomUserPrinciple principle = new CustomUserPrinciple(id, email);
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                principle,
//                null,
//                List.of(new SimpleGrantedAuthority("ROLE_" + role))
//        );
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(auth);
//        SecurityContextHolder.setContext(context);
//    }
//
//    /*
//    ME ENDPOINTS
//     */
//
//    @Test
//    void updateUsername_whenAuthenticated_returnsUpdatedUser() throws Exception {
//        setUserInSecurityContext(1, "user@test.com", "USER");
//
//        UserSummaryDto response = new UserSummaryDto(1, "newUsername", "user@test.com", RoleEnum.USER);
//        when(userService.updateUsernameById(any(), eq("newUsername"))).thenReturn(response);
//
//        UpdateUsernameDto request = new UpdateUsernameDto("newUsername");
//
//        mockMvc.perform(put("/api/v1/users/me/username")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("newUsername"));
//    }
//
//    @Test
//    void updateEmail_whenAuthenticated_returnsUpdatedUser() throws Exception {
//        setUserInSecurityContext(1, "user@test.com", "USER");
//
//        UserSummaryDto response = new UserSummaryDto(1, "test", "new@test.com", RoleEnum.USER);
//        when(userService.updateEmailById(eq(1), eq("new@test.com"))).thenReturn(response);
//
//        UpdateEmailDto request = new UpdateEmailDto("new@test.com");
//
//        mockMvc.perform(put("/api/v1/users/me/email")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("new@test.com"));
//    }
//
//    @Test
//    void updatePassword_whenAuthenticated_returnsUpdatedUser() throws Exception {
//        setUserInSecurityContext(1, "user@test.com", "USER");
//
//        UserSummaryDto response = new UserSummaryDto(1, "test", "user@test.com", RoleEnum.USER);
//        when(userService.updatePasswordById(eq(1), eq("newPassword123"))).thenReturn(response);
//
//        UpdatePasswordDto request = new UpdatePasswordDto("newPassword123");
//
//        mockMvc.perform(put("/api/v1/users/me/password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("user@test.com"));
//    }
//
//    /*
//    ADMIN ENDPOINTS
//     */
//
//    @Test
//    void updateUsernameAdmin_whenAdmin_returnsUpdatedUser() throws Exception {
//        setUserInSecurityContext(1, "admin@test.com", "ADMIN");
//
//        UserSummaryDto response = new UserSummaryDto(2, "newUsername", "other@test.com", RoleEnum.USER);
//        when(userService.updateUsernameById(eq(2), eq("newUsername"))).thenReturn(response);
//
//        UpdateUsernameAdminDto request = new UpdateUsernameAdminDto(2, "newUsername");
//
//        mockMvc.perform(put("/api/v1/users/admin/username")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("newUsername"));
//    }
//
//    @Test
//    void updateEmailAdmin_whenAdmin_returnsUpdatedUser() throws Exception {
//        setUserInSecurityContext(1, "admin@test.com", "ADMIN");
//
//        UserSummaryDto response = new UserSummaryDto(2, "test", "new@test.com", RoleEnum.USER);
//        when(userService.updateEmailById(eq(2), eq("new@test.com"))).thenReturn(response);
//
//        UpdateEmailAdminDto request = new UpdateEmailAdminDto(2, "new@test.com");
//
//        mockMvc.perform(put("/api/v1/users/admin/email")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("new@test.com"));
//    }
//
//    @Test
//    void updatePasswordAdmin_whenAdmin_returnsUpdatedUser() throws Exception {
//        setUserInSecurityContext(1, "admin@test.com", "ADMIN");
//
//        UserSummaryDto response = new UserSummaryDto(2, "test", "other@test.com", RoleEnum.USER);
//        when(userService.updatePasswordById(eq(2), eq("newPassword123"))).thenReturn(response);
//
//        UpdatePasswordAdminDto request = new UpdatePasswordAdminDto(2, "newPassword123");
//
//        mockMvc.perform(put("/api/v1/users/admin/password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(2));
//    }
//
//    @Test
//    void deleteUser_whenAdmin_returnsNoContent() throws Exception {
//        setUserInSecurityContext(1, "admin@test.com", "ADMIN");
//
//        mockMvc.perform(delete("/api/v1/users/2"))
//                .andExpect(status().isNoContent());
//
//        verify(userService).deleteUserById(2);
//    }
//
//    @Test
//    void getAllUsers_whenAdmin_returnsPageOfUsers() throws Exception {
//        setUserInSecurityContext(1, "admin@test.com", "ADMIN");
//
//        List<UserSummaryDto> users = List.of(
//                new UserSummaryDto(1, "user1", "user1@test.com", RoleEnum.USER),
//                new UserSummaryDto(2, "user2", "user2@test.com", RoleEnum.USER)
//        );
//        when(userService.getAllUsers(any())).thenReturn(new PageImpl<>(users, PageRequest.of(0, 10), 2));
//
//        mockMvc.perform(get("/api/v1/users?page=0&size=10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].username").value("user1"))
//                .andExpect(jsonPath("$.content[1].username").value("user2"))
//                .andExpect(jsonPath("$.totalElements").value(2));
//    }
//
//    @Test
//    void getUsersByFilters_whenAdmin_returnsFilteredUsers() throws Exception {
//        setUserInSecurityContext(1, "admin@test.com", "ADMIN");
//
//        List<UserSummaryDto> users = List.of(
//                new UserSummaryDto(1, "melle", "melle@test.com", RoleEnum.USER)
//        );
//        when(userService.getUsersByFilters(eq("melle"), isNull(), isNull(), any()))
//                .thenReturn(new PageImpl<>(users, PageRequest.of(0, 10), 1));
//
//        mockMvc.perform(get("/api/v1/users/search?username=melle"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].username").value("melle"))
//                .andExpect(jsonPath("$.totalElements").value(1));
//    }
//}