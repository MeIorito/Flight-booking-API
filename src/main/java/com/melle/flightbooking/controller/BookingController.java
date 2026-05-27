package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.CustomUserPrinciple;
import com.melle.flightbooking.dto.booking.BookingSummaryDto;
import com.melle.flightbooking.dto.booking.RegisterBookingDto;
import com.melle.flightbooking.dto.booking.UpdateBookingUserDto;
import com.melle.flightbooking.service.BookingServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    // Booking service
    private final BookingServiceImp bookingService;


    @Autowired
    public BookingController(BookingServiceImp bookingService) {
        this.bookingService = bookingService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public BookingSummaryDto createBooking(@RequestBody RegisterBookingDto request){
        CustomUserPrinciple user = getUserPrinciple();

        System.out.println(user.getId());
        System.out.println(request.getFlightId());

        return bookingService.createBooking(user.getId(), request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user")
    public BookingSummaryDto updateBookingUser(@RequestBody UpdateBookingUserDto request){
        return bookingService.updateBookingUser(request);
    }
//
//    @PreAuthorize("hasRole('USER')")
//    @DeleteMapping()
//    public BookingSummaryDto deleteBooking(){}
//
//    @PreAuthorize("hasRole('USER')")
//    @GetMapping()
//    public BookingSummaryDto getAllBookings(){}
//
//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/{id}")
//    public BookingSummaryDto getBookingById(){}

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
