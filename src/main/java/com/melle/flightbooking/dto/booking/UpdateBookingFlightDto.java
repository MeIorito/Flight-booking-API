package com.melle.flightbooking.dto.booking;

import jakarta.validation.constraints.NotNull;

import static com.melle.flightbooking.config.ValidationConstants.ID_BLANK_MESSAGE;

public class UpdateBookingFlightDto {
    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer bookingId;

    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer flightId;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }
}
