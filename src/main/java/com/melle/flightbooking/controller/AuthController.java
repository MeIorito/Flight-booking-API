package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.LoginRequestDto;
import com.melle.flightbooking.dto.LoginResponseDto;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserServiceImp userService;

    @Autowired
    public AuthController(UserServiceImp userService){ this.userService = userService; }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        return userService.login(request.getEmail(), request.getPassword());
    }

}
