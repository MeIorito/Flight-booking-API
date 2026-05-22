package com.melle.flightbooking.service;

import com.melle.flightbooking.config.JwtUtil;
import com.melle.flightbooking.dto.LoginResponseDto;
import com.melle.flightbooking.dto.UserSummaryDto;
import com.melle.flightbooking.exception.*;
import com.melle.flightbooking.interfaces.UserService;
import com.melle.flightbooking.model.RoleEnum;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public UserSummaryDto register(User newUser){
        boolean isEmailPresent = userRepository.existsByEmail(newUser.getEmail());

        if(isEmailPresent){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(RoleEnum.USER);

        User savedUser = userRepository.save(newUser);
        
        return new UserSummaryDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());
    }

    public UserSummaryDto updateUsernameById(Integer id, String username) {
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);
        newUser.setUsername(username);

        User savedUser = userRepository.save(newUser);

        return new UserSummaryDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());
    }

    public UserSummaryDto updateEmailById(Integer id, String email) {
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);
        newUser.setEmail(email);

        User savedUser = userRepository.save(newUser);

        return new UserSummaryDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());
    }

    public UserSummaryDto updatePasswordById(Integer id, String password) {
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);

        newUser.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(newUser);

        return new UserSummaryDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());
    }

    public UserSummaryDto updateRoleById(Integer id, RoleEnum role) {
        idIsPresent(id);

        User newUser = userRepository.findUserById(id);
        newUser.setRole(role);

        User savedUser = userRepository.save(newUser);

        return new UserSummaryDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());
    }

    public Boolean deleteUserById(Integer id){
        idIsPresent(id);

        userRepository.deleteById(id);
        return true;
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

        return new LoginResponseDto(new UserSummaryDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole()), jwtToken);
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

    private void idIsPresent(Integer id) {
        boolean idIsPresent = userRepository.existsById(id);

        if (!idIsPresent){
            throw new IdDoesNotExistException("Id does not exist");
        }
    }

}
