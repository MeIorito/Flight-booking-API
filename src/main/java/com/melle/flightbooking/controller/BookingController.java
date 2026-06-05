package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.CustomUserPrinciple;
import com.melle.flightbooking.dto.booking.*;
import com.melle.flightbooking.interfaces.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    // Booking service
    private final BookingService bookingService;

    /*
    USER ROLE ENDPOINTS
     */

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Creates booking with join of flight and user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "Flight id not found")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public BookingSummaryDto createBooking(@Valid @RequestBody RegisterBookingDto request) {
        CustomUserPrinciple user = getUserPrinciple();

        return bookingService.createBooking(user.getId(), request);
    }

    @Operation(summary = "Gets all bookings with given userId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully gotten all bookings with given userId"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "User id not found")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public Iterable<BookingSummaryDto> getUserBookings() {
        CustomUserPrinciple user = getUserPrinciple();

        return bookingService.getBookingsByUserId(user.getId());
    }

    @Operation(summary = "Gets booking with given bookingId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully gotten Booking"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{bookingId}")
    public BookingSummaryDto getBookingById(@Valid @PathVariable Integer bookingId) {
        CustomUserPrinciple user = getUserPrinciple();

        return bookingService.getBookingById(user.getId(), bookingId);
    }

    @Operation(summary = "Deletes booking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Username updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "404", description = "User id not found")
    })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping()
    public ResponseEntity<Void> deleteBooking(@Valid @RequestBody DeleteBookingDto request) {
        CustomUserPrinciple user = getUserPrinciple();
        bookingService.deleteBooking(user.getId(), request);
        return ResponseEntity.noContent().build();
    }

    /*
    ADMIN ROLE ENDPOINTS
     */

    @Operation(summary = "Updates user for given bookingId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "404", description = "User id not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user")
    public BookingSummaryDto updateBookingUser(@Valid @RequestBody UpdateBookingUserDto request) {
        return bookingService.updateBookingUser(request);
    }

    @Operation(summary = "Updates flight for given bookingId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flight updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "404", description = "Flight id not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/flight")
    public BookingSummaryDto updateBookingFlight(@Valid @RequestBody UpdateBookingFlightDto request){
        return bookingService.updateBookingFlight(request);
    }

    @Operation(summary = "Deleted booking with given bookingId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "Booking id not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBookingAdmin(@Valid @PathVariable Integer bookingId) {
        bookingService.deleteBookingAdmin(bookingId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Gets all bookings with given userId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bookings successfully gotten"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
            @ApiResponse(responseCode = "404", description = "User id not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public Iterable<BookingSummaryDto> getBookingsByUserId(@PathVariable Integer userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @Operation(summary = "Gets all bookings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bookings successfully gotten"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bad jwt token"),
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public Iterable<BookingSummaryDto> getAllBookings() {
        return bookingService.getAllBookings();
    }

    /*
    HELPER FUNCTIONS
     */

    @Operation(summary = "Updates username of submitting user")
    private CustomUserPrinciple getUserPrinciple() {
        return (CustomUserPrinciple) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
