package com.melle.flightbooking.dto.flight;
import jakarta.validation.constraints.*;

import static com.melle.flightbooking.config.ValidationConstants.* ;

public class UpdateSeatsDto {
    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer id;

    @NotNull(message = SEATS_NULL_MESSAGE)
    @Min(value = SEATS_MIN, message = SEATS_MIN_MESSAGE)
    @Max(value = SEATS_MAX, message = SEATS_MAX_MESSAGE)
    private Integer seats;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
