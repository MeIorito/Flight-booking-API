package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.UserSummaryDto;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImp userService;

    @Autowired
    public UserController(UserServiceImp userService){ this.userService = userService; }

    // Should not return Boolean
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Boolean deleteUser(@PathVariable Integer id){ return this.userService.deleteUserById(id); }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Iterable<UserSummaryDto> getAllUsers(){ return this.userService.getAllUsers();}
}
