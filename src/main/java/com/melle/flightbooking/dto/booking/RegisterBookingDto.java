package com.melle.flightbooking.dto.booking;

import jakarta.validation.constraints.NotNull;

import static com.melle.flightbooking.config.ValidationConstants.ID_BLANK_MESSAGE;

public class RegisterBookingDto {

    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer flightId;

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }
}
