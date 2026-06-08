package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.*;
import com.melle.flightbooking.dto.user.UpdateEmailAdminDto;
import com.melle.flightbooking.dto.user.UpdatePasswordAdminDto;
import com.melle.flightbooking.dto.user.UpdateUsernameAdminDto;
import com.melle.flightbooking.dto.user.UserSummaryDto;
import com.melle.flightbooking.dto.user.UpdateEmailDto;
import com.melle.flightbooking.dto.user.UpdatePasswordDto;
import com.melle.flightbooking.dto.user.UpdateUsernameDto;
import com.melle.flightbooking.interfaces.UserService;
import com.melle.flightbooking.model.RoleEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){ this.userService = userService; }

    /*
    USER AND ABOVE ONLY ENDPOINTS
     */

    @Operation(summary = "Updates username of submitting user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Username updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "User id not found")
    })
    @PutMapping("/me/username")
    public UserSummaryDto updateUsername(@Valid @RequestBody UpdateUsernameDto username) {
        CustomUserPrinciple user = getUserPrinciple();

        return this.userService.updateUsernameById(user.getId(), username.getUsername());
    }

    @Operation(summary = "Updates email of submitting user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "User id not found")
    })
    @PutMapping("/me/email")
    public UserSummaryDto updateEmail(@Valid @RequestBody UpdateEmailDto email) {
        CustomUserPrinciple user = getUserPrinciple();

        return this.userService.updateEmailById(user.getId(), email.getEmail());
    }

    @Operation(summary = "Updates password of submitting user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "User id not found")
    })
    @PutMapping("/me/password")
    public UserSummaryDto updatePassword(@Valid @RequestBody UpdatePasswordDto password) {
        CustomUserPrinciple user = getUserPrinciple();

        return this.userService.updatePasswordById(user.getId(), password.getPassword());
    }

    /*
    ADMIN AND ABOVE ONLY ENDPOINTS
     */

    @Operation(summary = "Updates username of given userId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Username updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "User id not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/username")
    public UserSummaryDto updateUsernameAdmin(@Valid @RequestBody UpdateUsernameAdminDto username) {

        return this.userService.updateUsernameById(username.getId(), username.getUsername());
    }

    @Operation(summary = "Updates email of given userId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "User id not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/email")
    public UserSummaryDto updateEmailAdmin(@Valid @RequestBody UpdateEmailAdminDto email) {

        return this.userService.updateEmailById(email.getId(), email.getEmail());
    }

    @Operation(summary = "Updates password of given userId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "User id not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/password")
    public UserSummaryDto updatePasswordAdmin(@Valid @RequestBody UpdatePasswordAdminDto password) {

        return this.userService.updatePasswordById(password.getId(), password.getPassword());
    }

    @Operation(summary = "Deletes user with given userId")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully deleted, no response"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "User id not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Gets all users back in Iterable form")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully gotten all users"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<UserSummaryDto> getAllUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        Pageable pageable = PageRequest.of(page, size);

        return this.userService.getAllUsers(pageable);
    }

    @Operation(summary = "Gets filtered users back in Iterable form")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully gotten filtered users"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public Iterable<UserSummaryDto> getUsersByFilters(
            @RequestParam (required = false) String username,
            @RequestParam (required = false) String email,
            @RequestParam (required = false) RoleEnum role,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return userService.getUsersByFilters(username, email, role, pageable);
    }

    /*
    HELPER FUNCTIONS
     */

    @Operation(summary = "Function that gets user from security context")
    private CustomUserPrinciple getUserPrinciple() {
        return (CustomUserPrinciple) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
