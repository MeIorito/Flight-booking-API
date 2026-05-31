package com.melle.flightbooking.dto.booking;

import jakarta.validation.constraints.NotNull;

import static com.melle.flightbooking.config.ValidationConstants.ID_BLANK_MESSAGE;

public class DeleteBookingDto {
    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer bookingId;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
}
