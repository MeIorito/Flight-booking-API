package com.melle.flightbooking.service;

import com.melle.flightbooking.exception.EmailAlreadyExistsException;
import com.melle.flightbooking.exception.EmailDoesNotExistException;
import com.melle.flightbooking.exception.InvalidCredentialsException;
import com.melle.flightbooking.interfaces.UserService;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User register(User newUser){
        boolean isEmailPresent = userRepository.existsByEmail(newUser.getEmail());

        if(isEmailPresent){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        return userRepository.save(newUser);
    }

    // HASHING!!
    public User login(String email, String password){
        boolean isEmailPresent = userRepository.existsByEmail(email);

        if(!isEmailPresent){
            throw new EmailDoesNotExistException("Email does not exist");
        }

        User user = userRepository.findUserByEmail(email);

        if(!Objects.equals(user.getPassword(), password)){
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return user;
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
