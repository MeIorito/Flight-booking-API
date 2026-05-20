package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.UserSummaryDto;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImp userService;

    @Autowired
    public UserController(UserServiceImp userService){ this.userService = userService; }

    // Should not return Boolean
    @DeleteMapping("/{id}")
    public Boolean deleteUser(@PathVariable Integer id){ return this.userService.deleteUserById(id); }

    @GetMapping
    public Iterable<UserSummaryDto> getAllUsers(){ return this.userService.getAllUsers();}
}
