package com.melle.flightbooking.interfaces;

import com.melle.flightbooking.model.User;

import java.util.Optional;

public interface UserService {
    User register(User user);
    User login(String email, String password);
    Optional<User> findByEmail(String email);
    Iterable<User> getAllUsers();
}
