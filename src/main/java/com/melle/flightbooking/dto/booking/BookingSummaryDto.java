package com.melle.flightbooking.dto.booking;

import com.melle.flightbooking.dto.flight.FlightSummaryDto;
import com.melle.flightbooking.dto.user.UserSummaryDto;

import java.time.LocalDateTime;

public class BookingSummaryDto {
    private Integer id;

    private Integer userId;
    private String username;

    private Integer flightId;
    private String origin;
    private String destination;

    private LocalDateTime bookedAt;

    public BookingSummaryDto(Integer id, Integer userId, String username, Integer flightId, String origin, String destination, LocalDateTime bookedAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.bookedAt = bookedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
}

