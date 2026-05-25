package com.melle.flightbooking.dto.flight;
import static com.melle.flightbooking.config.ValidationConstants.* ;

import jakarta.validation.constraints.*;

public class RegisterFlightDto {

    @NotNull(message = ORIGIN_BLANK_MESSAGE)
    @Size(min = ORIGIN_MIN, max = ORIGIN_MAX, message = ORIGIN_SIZE_MESSAGE)
    private String origin;

    @NotBlank(message = DESTINATION_BLANK_MESSAGE)
    @Size(min = DESTINATION_MIN, max = DESTINATION_MAX, message = DESTINATION_SIZE_MESSAGE)
    private String destination;

    @NotBlank(message = DATE_BLANK_MESSAGE)
    private String date;

    @NotNull(message = SEATS_NULL_MESSAGE)
    @Min(value = SEATS_MIN, message = SEATS_MIN_MESSAGE)
    @Max(value = SEATS_MAX, message = SEATS_MAX_MESSAGE)
    private Integer seats;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
