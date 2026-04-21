package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.LoginRequestDto;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserServiceImp userService;

    @Autowired
    public UserController(UserServiceImp userService){ this.userService = userService; }

    @PostMapping("/users")
    public User createUser(@RequestBody User user){ return this.userService.register(user);}

    @PostMapping("/users/login")
    public User userLogin(@RequestBody LoginRequestDto loginRequest){
        return this.userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @GetMapping("/users")
    public Iterable<User> getAllUsers(){ return this.userService.getAllUsers();}
}
