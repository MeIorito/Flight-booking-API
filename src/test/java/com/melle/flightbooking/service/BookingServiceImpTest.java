package com.melle.flightbooking.service;

import com.melle.flightbooking.dto.booking.*;
import com.melle.flightbooking.exception.BookingOwnershipException;
import com.melle.flightbooking.exception.FlightNotFoundException;
import com.melle.flightbooking.exception.IdDoesNotExistException;
import com.melle.flightbooking.model.Booking;
import com.melle.flightbooking.model.Flight;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.BookingRepository;
import com.melle.flightbooking.repository.FlightRepository;
import com.melle.flightbooking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImpTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private BookingServiceImp bookingService;

    // Helper methods
    private User createUser(Integer id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(username + "@test.com");
        return user;
    }

    private Flight createFlight(Integer id, String origin, String destination) {
        return new Flight(id, origin, destination, "2025-06-01", 100);
    }

    private Booking createBooking(Integer id, User user, Flight flight) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setBookedAt(LocalDateTime.now());
        return booking;
    }

    @Test
    void createBooking_whenUserAndFlightExist_returnsBookingSummaryDto() {
        User user = createUser(1, "user");
        Flight flight = createFlight(1, "Amsterdam", "London");
        Booking savedBooking = createBooking(1, user, flight);

        RegisterBookingDto request = new RegisterBookingDto();
        request.setFlightId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(flightRepository.findById(1)).thenReturn(Optional.of(flight));
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        BookingSummaryDto result = bookingService.createBooking(1, request);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("Amsterdam", result.getOrigin());
        assertEquals("London", result.getDestination());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_whenUserNotFound_throwsIdDoesNotExistException() {
        RegisterBookingDto request = new RegisterBookingDto();
        request.setFlightId(1);

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(IdDoesNotExistException.class,
                () -> bookingService.createBooking(99, request));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_whenFlightNotFound_throwsFlightNotFoundException() {
        User user = createUser(1, "user");
        RegisterBookingDto request = new RegisterBookingDto();
        request.setFlightId(99);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(flightRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class,
                () -> bookingService.createBooking(1, request));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void updateBookingUser_whenBookingAndUserExist_returnsUpdatedBooking() {
        User oldUser = createUser(1, "user");
        User newUser = createUser(2, "newUser");
        Flight flight = createFlight(1, "Amsterdam", "London");
        Booking booking = createBooking(1, oldUser, flight);

        UpdateBookingUserDto request = new UpdateBookingUserDto();
        request.setBookingId(1);
        request.setUserId(2);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(userRepository.findById(2)).thenReturn(Optional.of(newUser));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        BookingSummaryDto result = bookingService.updateBookingUser(request);

        assertEquals(2, result.getUserId());
        assertEquals("newUser", result.getUsername());
    }

    @Test
    void updateBookingUser_whenBookingNotFound_throwsFlightNotFoundException() {
        UpdateBookingUserDto request = new UpdateBookingUserDto();
        request.setBookingId(99);
        request.setUserId(1);

        when(bookingRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class,
                () -> bookingService.updateBookingUser(request));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void getBookingsByUserId_returnsPageOfBookings() {
        User user = createUser(1, "user");
        Flight flight = createFlight(1, "Amsterdam", "London");
        Booking booking = createBooking(1, user, flight);
        Page<Booking> bookingPage = new PageImpl<>(List.of(booking), PageRequest.of(0, 10), 1);

        when(bookingRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookingPage);

        Page<BookingSummaryDto> result = bookingService.getBookingsByUserId(1, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Amsterdam", result.getContent().get(0).getOrigin());
    }

    @Test
    void updateBookingFlight_whenBookingAndFlightExist_returnsUpdatedBooking() {
        User user = createUser(1, "user");
        Flight oldFlight = createFlight(1, "Amsterdam", "London");
        Flight newFlight = createFlight(2, "Rotterdam", "Paris");
        Booking booking = createBooking(1, user, oldFlight);

        UpdateBookingFlightDto request = new UpdateBookingFlightDto();
        request.setBookingId(1);
        request.setFlightId(2);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(flightRepository.findById(2)).thenReturn(Optional.of(newFlight));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        BookingSummaryDto result = bookingService.updateBookingFlight(request);

        assertEquals("Rotterdam", result.getOrigin());
        assertEquals("Paris", result.getDestination());
    }

    @Test
    void updateBookingFlight_whenFlightNotFound_throwsFlightNotFoundException() {
        User user = createUser(1, "user");
        Flight flight = createFlight(1, "Amsterdam", "London");
        Booking booking = createBooking(1, user, flight);

        UpdateBookingFlightDto request = new UpdateBookingFlightDto();
        request.setBookingId(1);
        request.setFlightId(99);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(flightRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class,
                () -> bookingService.updateBookingFlight(request));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void deleteBooking_whenOwner_deletesBooking() {
        User user = createUser(1, "user");
        Flight flight = createFlight(1, "Amsterdam", "London");
        Booking booking = createBooking(1, user, flight);

        DeleteBookingDto request = new DeleteBookingDto();
        request.setBookingId(1);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        bookingService.deleteBooking(1, request);

        verify(bookingRepository).delete(booking);
    }

    @Test
    void deleteBooking_whenNotOwner_throwsBookingOwnershipException() {
        User user = createUser(1, "user");
        Flight flight = createFlight(1, "Amsterdam", "London");
        Booking booking = createBooking(1, user, flight);

        DeleteBookingDto request = new DeleteBookingDto();
        request.setBookingId(1);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        assertThrows(BookingOwnershipException.class,
                () -> bookingService.deleteBooking(99, request));

        verify(bookingRepository, never()).delete(any(Booking.class));
    }

    @Test
    void deleteBooking_whenBookingNotFound_throwsFlightNotFoundException() {
        DeleteBookingDto request = new DeleteBookingDto();
        request.setBookingId(99);

        when(bookingRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class,
                () -> bookingService.deleteBooking(1, request));

        verify(bookingRepository, never()).delete(any(Booking.class));
    }

    @Test
    void deleteBookingAdmin_whenBookingExists_deletesBooking() {
        User user = createUser(1, "user");
        Flight flight = createFlight(1, "Amsterdam", "London");
        Booking booking = createBooking(1, user, flight);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        bookingService.deleteBookingAdmin(1);

        verify(bookingRepository).delete(booking);
    }

    @Test
    void deleteBookingAdmin_whenBookingNotFound_throwsFlightNotFoundException() {
        when(bookingRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class,
                () -> bookingService.deleteBookingAdmin(99));

        verify(bookingRepository, never()).delete(any(Booking.class));
    }

    @Test
    void getBookingById_whenOwner_returnsBookingSummaryDto() {
        User user = createUser(1, "user");
        Flight flight = createFlight(1, "Amsterdam", "London");
        Booking booking = createBooking(1, user, flight);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        BookingSummaryDto result = bookingService.getBookingById(1, 1);

        assertEquals("Amsterdam", result.getOrigin());
        assertEquals(1, result.getUserId());
    }

    @Test
    void getBookingById_whenNotOwner_throwsBookingOwnershipException() {
        User user = createUser(1, "user");
        Flight flight = createFlight(1, "Amsterdam", "London");
        Booking booking = createBooking(1, user, flight);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        assertThrows(BookingOwnershipException.class,
                () -> bookingService.getBookingById(99, 1));
    }

    @Test
    void getBookingById_whenBookingNotFound_throwsFlightNotFoundException() {
        when(bookingRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class,
                () -> bookingService.getBookingById(1, 99));
    }

    @Test
    void getAllBookings_returnsPageOfBookings() {
        User user = createUser(1, "user");
        Flight flight = createFlight(1, "Amsterdam", "London");
        Booking booking = createBooking(1, user, flight);
        Page<Booking> bookingPage = new PageImpl<>(List.of(booking), PageRequest.of(0, 10), 1);

        when(bookingRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookingPage);

        Page<BookingSummaryDto> result = bookingService.getAllBookings(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Amsterdam", result.getContent().get(0).getOrigin());
    }
}