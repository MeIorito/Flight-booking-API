package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.*;
import com.melle.flightbooking.dto.user.UpdateEmailAdminDto;
import com.melle.flightbooking.dto.user.UpdatePasswordAdminDto;
import com.melle.flightbooking.dto.user.UpdateUsernameAdminDto;
import com.melle.flightbooking.dto.user.UserSummaryDto;
import com.melle.flightbooking.dto.user.UpdateEmailDto;
import com.melle.flightbooking.dto.user.UpdatePasswordDto;
import com.melle.flightbooking.dto.user.UpdateUsernameDto;
import com.melle.flightbooking.service.UserServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImp userService;

    @Autowired
    public UserController(UserServiceImp userService){ this.userService = userService; }

    /*
    USER AND ABOVE ONLY ENDPOINTS
     */

    @PutMapping("/me/username")
    public UserSummaryDto updateUsername(@Valid @RequestBody UpdateUsernameDto username) {
        CustomUserPrinciple user = getUserPrinciple();

        return this.userService.updateUsernameById(user.getId(), username.getUsername());
    }

    @PutMapping("/me/email")
    public UserSummaryDto updateEmail(@Valid @RequestBody UpdateEmailDto email) {
        CustomUserPrinciple user = getUserPrinciple();

        return this.userService.updateEmailById(user.getId(), email.getEmail());
    }

    @PutMapping("/me/password")
    public UserSummaryDto updatePassword(@Valid @RequestBody UpdatePasswordDto password) {
        CustomUserPrinciple user = getUserPrinciple();

        return this.userService.updatePasswordById(user.getId(), password.getPassword());
    }

    /*
    ADMIN AND ABOVE ONLY ENDPOINTS
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/username")
    public UserSummaryDto updateUsernameAdmin(@Valid @RequestBody UpdateUsernameAdminDto username) {

        return this.userService.updateUsernameById(username.getId(), username.getUsername());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/email")
    public UserSummaryDto updateEmailAdmin(@Valid @RequestBody UpdateEmailAdminDto email) {

        return this.userService.updateEmailById(email.getId(), email.getEmail());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/password")
    public UserSummaryDto updatePasswordAdmin(@Valid @RequestBody UpdatePasswordAdminDto password) {

        return this.userService.updatePasswordById(password.getId(), password.getPassword());
    }

    // Should not return Boolean
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Boolean deleteUser(@PathVariable Integer id){ return this.userService.deleteUserById(id); }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Iterable<UserSummaryDto> getAllUsers(){ return this.userService.getAllUsers();}

    /*
    HELPER FUNCTIONS
     */

    private CustomUserPrinciple getUserPrinciple() {
        return (CustomUserPrinciple) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
