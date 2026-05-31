package com.melle.flightbooking.dto.booking;

import jakarta.validation.constraints.NotNull;

import static com.melle.flightbooking.config.ValidationConstants.ID_BLANK_MESSAGE;

public class UpdateBookingUserDto {
    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer bookingId;

    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer userId;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
