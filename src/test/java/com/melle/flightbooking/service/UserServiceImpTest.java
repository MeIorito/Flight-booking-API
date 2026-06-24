package com.melle.flightbooking.service;

import com.melle.flightbooking.config.JwtUtil;
import com.melle.flightbooking.dto.auth.LoginRequestDto;
import com.melle.flightbooking.dto.auth.LoginResponseDto;
import com.melle.flightbooking.dto.auth.RegisterRequestDto;
import com.melle.flightbooking.dto.user.UserSummaryDto;
import com.melle.flightbooking.exception.EmailAlreadyExistsException;
import com.melle.flightbooking.exception.EmailDoesNotExistException;
import com.melle.flightbooking.exception.IdDoesNotExistException;
import com.melle.flightbooking.exception.InvalidCredentialsException;
import com.melle.flightbooking.model.RoleEnum;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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

    @Test
    void login_whenEmailNotInUse_throwsEmailDoesNotExistException() {
        LoginRequestDto request = new LoginRequestDto("user@test.com", "password123");

        when(userRepository.existsByEmail("user@test.com")).thenReturn(false);

        assertThrows(EmailDoesNotExistException.class,
                () -> userService.login(request));

        verify(userRepository, never()).findUserByEmail(any());
    }

    @Test
    void login_whenPasswordIsIncorrect_throwsInvalidCredentialsException() {
        LoginRequestDto request = new LoginRequestDto("user@test.com", "wrongpassword");

        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword("hashedPassword");

        when(userRepository.existsByEmail("user@test.com")).thenReturn(true);
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(user);
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> userService.login(request));

        verify(jwtUtil, never()).createToken(any(), any());
    }

    @Test
    void updateUsernameById_whenUserExists_returnsUpdatedUserSummaryDto() {
        User user = new User();
        user.setId(1);
        user.setUsername("oldUsername");
        user.setEmail("user@test.com");
        user.setRole(RoleEnum.USER);

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.findUserById(user.getId())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserSummaryDto result = userService.updateUsernameById(1, "newUsername");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("newUsername", captor.getValue().getUsername());
    }

    @Test
    void updateUsernameById_whenUserDoesNotExist_returnsIdDoesNotExistException() {
        User user = new User();
        user.setId(1);
        user.setUsername("oldUsername");
        user.setEmail("user@test.com");
        user.setRole(RoleEnum.USER);

        when(userRepository.existsById(user.getId())).thenReturn(false);

        assertThrows(IdDoesNotExistException.class,
                () -> userService.updateUsernameById(user.getId(), user.getUsername()));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateEmailById_whenUserExists_returnsUpdatedUserSummaryDto() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setEmail("old@test.com");
        user.setRole(RoleEnum.USER);

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.findUserById(user.getId())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        userService.updateEmailById(1, "new@test.com");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("new@test.com", captor.getValue().getEmail());
    }

    @Test
    void updateEmailById_whenUserDoesNotExist_throwsIdDoesNotExistException() {
        User user = new User();
        user.setId(1);

        when(userRepository.existsById(user.getId())).thenReturn(false);

        assertThrows(IdDoesNotExistException.class,
                () -> userService.updateEmailById(user.getId(), "new@test.com"));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updatePasswordById_whenUserExists_savesEncodedPassword() {
        User user = new User();
        user.setId(1);
        user.setPassword("oldHashedPassword");

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.findUserById(user.getId())).thenReturn(user);
        when(passwordEncoder.encode("newPassword123")).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        userService.updatePasswordById(1, "newPassword123");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("newHashedPassword", captor.getValue().getPassword());
    }

    @Test
    void updatePasswordById_whenUserDoesNotExist_throwsIdDoesNotExistException() {
        User user = new User();
        user.setId(1);

        when(userRepository.existsById(user.getId())).thenReturn(false);

        assertThrows(IdDoesNotExistException.class,
                () -> userService.updatePasswordById(user.getId(), "newPassword123"));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateRoleById_whenUserExists_returnsUpdatedUserSummaryDto() {
        User user = new User();
        user.setId(1);
        user.setRole(RoleEnum.USER);

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.findUserById(user.getId())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        userService.updateRoleById(1, RoleEnum.ADMIN);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals(RoleEnum.ADMIN, captor.getValue().getRole());
    }

    @Test
    void updateRoleById_whenUserDoesNotExist_throwsIdDoesNotExistException() {
        User user = new User();
        user.setId(1);

        when(userRepository.existsById(user.getId())).thenReturn(false);

        assertThrows(IdDoesNotExistException.class,
                () -> userService.updateRoleById(user.getId(), RoleEnum.ADMIN));

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUserById_whenUserExists_deletesUser() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUserById(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    void deleteUserById_whenUserDoesNotExist_throwsIdDoesNotExistException() {
        User user = new User();
        user.setId(1);

        when(userRepository.existsById(user.getId())).thenReturn(false);
        assertThrows(IdDoesNotExistException.class,
                () -> userService.deleteUserById(user.getId()));

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void findByEmail_whenUserExists_returnsUserSummaryDto() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setEmail("user@test.com");
        user.setRole(RoleEnum.USER);

        when(userRepository.existsByEmail("user@test.com")).thenReturn(true);
        when(userRepository.findUserByEmail("user@test.com")).thenReturn(user);

        UserSummaryDto result = userService.findByEmail("user@test.com");

        assertEquals("user@test.com", result.email());
        assertEquals("test", result.username());
    }

    @Test
    void findByEmail_whenUserDoesNotExist_throwsEmailDoesNotExistException() {
        when(userRepository.existsByEmail("user@test.com")).thenReturn(false);

        assertThrows(EmailDoesNotExistException.class,
                () -> userService.findByEmail("user@test.com"));

        verify(userRepository, never()).findUserByEmail(any());
    }
}