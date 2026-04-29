package com.melle.flightbooking.service;

import com.melle.flightbooking.config.JwtFilter;
import com.melle.flightbooking.config.JwtUtil;
import com.melle.flightbooking.dto.LoginResponseDto;
import com.melle.flightbooking.exception.EmailAlreadyExistsException;
import com.melle.flightbooking.exception.EmailDoesNotExistException;
import com.melle.flightbooking.exception.InvalidCredentialsException;
import com.melle.flightbooking.interfaces.UserService;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    public User register(User newUser){
        boolean isEmailPresent = userRepository.existsByEmail(newUser.getEmail());

        if(isEmailPresent){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }

    public Boolean deleteUserById(Integer id){
        boolean isIdPresent = userRepository.existsById(id);

        if(!isIdPresent){
            // New IdNotFoundException
            throw new EmailDoesNotExistException("Email does not exist");
        }

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
        claims.put("role", "user");
        String jwtToken = jwtUtil.createToken(claims, email);

        return new LoginResponseDto(user, jwtToken);
    }

    public Optional<User> findByEmail(String email){
        boolean isEmailPresent = userRepository.existsByEmail(email);

        if(!isEmailPresent){
            throw new EmailDoesNotExistException("Email does not exist");
        }

        return Optional.ofNullable(userRepository.findUserByEmail(email));
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }
}
