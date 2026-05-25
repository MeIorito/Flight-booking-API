package com.melle.flightbooking.dto.flight;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.melle.flightbooking.config.ValidationConstants.* ;

public class UpdateOriginDto {
    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer id;

    @NotBlank(message = ORIGIN_BLANK_MESSAGE)
    @Size(min = ORIGIN_MIN, max = ORIGIN_MAX, message = ORIGIN_SIZE_MESSAGE)
    private String origin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
