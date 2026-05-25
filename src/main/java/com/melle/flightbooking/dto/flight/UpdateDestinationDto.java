package com.melle.flightbooking.dto.flight;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.melle.flightbooking.config.ValidationConstants.* ;

public class UpdateDestinationDto {
    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer id;

    @NotBlank(message = DESTINATION_BLANK_MESSAGE)
    @Size(min = DESTINATION_MIN, max = DESTINATION_MAX, message = DESTINATION_SIZE_MESSAGE)
    private String destination;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
