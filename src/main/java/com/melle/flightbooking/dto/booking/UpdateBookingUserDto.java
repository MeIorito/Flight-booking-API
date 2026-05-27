package com.melle.flightbooking.dto.booking;

public class UpdateBookingUserDto {
    private Integer bookingId;
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
