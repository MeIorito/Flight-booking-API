package com.melle.flightbooking.service;

import com.melle.flightbooking.config.JwtUtil;
import com.melle.flightbooking.dto.auth.LoginResponseDto;
import com.melle.flightbooking.dto.auth.RegisterRequestDto;
import com.melle.flightbooking.dto.user.UserSummaryDto;
import com.melle.flightbooking.exception.*;
import com.melle.flightbooking.interfaces.UserService;
import com.melle.flightbooking.model.RoleEnum;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.UserRepository;
import com.melle.flightbooking.specifications.UserSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserSummaryDto register(RegisterRequestDto request){
        log.info("Creating new user with username: {} and email: {}", request.getUsername(), request.getEmail());
        boolean isEmailPresent = userRepository.existsByEmail(request.getEmail());

        if(isEmailPresent){
            log.warn("Email: {} is already in use", request.getEmail());
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User newUser = new User();

        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(RoleEnum.USER);

        User savedUser = userRepository.save(newUser);
        log.info("User created successfully with id: {}", savedUser.getId());
        
        return createUserSummaryDto(savedUser);
    }

    public UserSummaryDto updateUsernameById(Integer id, String username) {
        log.info("Updating username of user with id: {} to: {}", id, username);
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);
        newUser.setUsername(username);

        User savedUser = userRepository.save(newUser);
        log.info("Username of user with id: {} successfully updated to: {}", savedUser.getId(), savedUser.getUsername());

        return createUserSummaryDto(savedUser);
    }

    public UserSummaryDto updateEmailById(Integer id, String email) {
        log.info("Updating email of user with id: {} to: {}", id, email);
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);
        newUser.setEmail(email);

        User savedUser = userRepository.save(newUser);
        log.info("Email of user with id: {} successfully updated to: {}", savedUser.getId(), savedUser.getEmail());

        return createUserSummaryDto(savedUser);
    }

    public UserSummaryDto updatePasswordById(Integer id, String password) {
        log.info("Updating password of user with id: {}", id);
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);

        newUser.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(newUser);
        log.info("Password of user with id: {} successfully updated", savedUser.getId());

        return createUserSummaryDto(savedUser);
    }

    public UserSummaryDto updateRoleById(Integer id, RoleEnum role) {
        log.info("Updating role of user with id: {} to: {}", id, role);
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);
        newUser.setRole(role);

        User savedUser = userRepository.save(newUser);
        log.info("Role of user with id: {} successfully updated to: {}", savedUser.getId(), savedUser.getRole());

        return createUserSummaryDto(savedUser);
    }

    public void deleteUserById(Integer id){
        log.info("Deleting user with id: {}", id);
        idIsPresent(id);

        userRepository.deleteById(id);
        log.info("User with id: {} successfully deleted", id);
    }

    public LoginResponseDto login(String email, String password){
        log.info("Logging in user with email: {}", email);
        boolean isEmailPresent = userRepository.existsByEmail(email);

        if(!isEmailPresent){
            log.warn("Email: {} is not in use", email);
            throw new EmailDoesNotExistException("Email does not exist");
        }

        User user = userRepository.findUserByEmail(email);

        if(!passwordEncoder.matches(password, user.getPassword())){
            log.warn("Credentials don't match for login user with email: {}", email);
            throw new InvalidCredentialsException("Invalid credentials");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("id", user.getId());
        String jwtToken = jwtUtil.createToken(claims, email);
        log.info("Successfully created jwt token for user with email: {}", email);

        return new LoginResponseDto(createUserSummaryDto(user), jwtToken);
    }

    public Optional<User> findByEmail(String email){
        log.info("Fetching user with email: {}", email);
        boolean isEmailPresent = userRepository.existsByEmail(email);

        if (!isEmailPresent) {
            log.warn("User with email: {} does not exist", email);
            throw new EmailDoesNotExistException("Email does not exist");
        }

        return Optional.ofNullable(userRepository.findUserByEmail(email));
    }

    public Page<UserSummaryDto> getAllUsers(Pageable pageable){
        log.info("Fetching all users");
        return userRepository.findBy(UserSummaryDto.class, pageable);
    }
    public Page<UserSummaryDto> getUsersByFilters(String username, String email, RoleEnum role, Pageable pageable) {
        log.info("Fetching users with filters - username: {}, email: {}, role: {}", username, email, role);

        Specification<User> spec = Specification.where(null);

        if (username != null && !username.isBlank()) {
            spec = spec.and(UserSpecifications.hasUsername(username));
        }

        if (email != null && !email.isBlank()) {
            spec = spec.and(UserSpecifications.hasEmail(email));
        }

        if (role != null) {
            spec = spec.and(UserSpecifications.hasRole(role));
        }

        Page<User> filteredUsers = userRepository.findAll(spec, pageable);
        log.info("Found {} users matching filters", filteredUsers.getSize());

        return filteredUsers.map(this::createUserSummaryDto);
    }

    /*
    HELPER FUNCTIONS
     */

    private UserSummaryDto createUserSummaryDto(User user) {
        return new UserSummaryDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    private void idIsPresent(Integer id) {
        boolean idIsPresent = userRepository.existsById(id);

        if (!idIsPresent){
            log.warn("User with id: {} does not exist", id);
            throw new IdDoesNotExistException("Id does not exist");
        }
    }

}
