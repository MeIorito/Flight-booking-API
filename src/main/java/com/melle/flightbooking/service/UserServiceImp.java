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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

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
        boolean isEmailPresent = userRepository.existsByEmail(request.getEmail());

        if(isEmailPresent){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User newUser = new User();

        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(RoleEnum.USER);

        User savedUser = userRepository.save(newUser);
        
        return createUserSummaryDto(savedUser);
    }

    public UserSummaryDto updateUsernameById(Integer id, String username) {
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);
        newUser.setUsername(username);

        User savedUser = userRepository.save(newUser);

        return createUserSummaryDto(savedUser);
    }

    public UserSummaryDto updateEmailById(Integer id, String email) {
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);
        newUser.setEmail(email);

        User savedUser = userRepository.save(newUser);

        return createUserSummaryDto(savedUser);
    }

    public UserSummaryDto updatePasswordById(Integer id, String password) {
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);

        newUser.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(newUser);

        return createUserSummaryDto(savedUser);
    }

    public UserSummaryDto updateRoleById(Integer id, RoleEnum role) {
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);
        newUser.setRole(role);

        User savedUser = userRepository.save(newUser);

        return createUserSummaryDto(savedUser);
    }

    public void deleteUserById(Integer id){
        idIsPresent(id);

        userRepository.deleteById(id);
    }

    public LoginResponseDto login(String email, String password){
        boolean isEmailPresent = userRepository.existsByEmail(email);

        if(!isEmailPresent){
            throw new EmailDoesNotExistException("Email does not exist");
        }

        User user = userRepository.findUserByEmail(email);

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new InvalidCredentialsException("Invalid credentials");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("id", user.getId());
        String jwtToken = jwtUtil.createToken(claims, email);

        return new LoginResponseDto(createUserSummaryDto(user), jwtToken);
    }

    public Optional<User> findByEmail(String email){
        boolean isEmailPresent = userRepository.existsByEmail(email);

        if(!isEmailPresent){
            throw new EmailDoesNotExistException("Email does not exist");
        }

        return Optional.ofNullable(userRepository.findUserByEmail(email));
    }

    public Iterable<UserSummaryDto> getAllUsers(){
        return userRepository.findBy(UserSummaryDto.class);
    }

    public Iterable<UserSummaryDto> getUsersByFilters(String username, String email, RoleEnum role) {

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

        Iterable<User> filteredUsers = userRepository.findAll(spec);

        return StreamSupport.stream(filteredUsers.spliterator(), false)
                .map(this::createUserSummaryDto)
                .toList();
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
            throw new IdDoesNotExistException("Id does not exist");
        }
    }

}
