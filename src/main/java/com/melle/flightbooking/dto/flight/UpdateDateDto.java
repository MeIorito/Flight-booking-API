package com.melle.flightbooking.dto.flight;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.melle.flightbooking.config.ValidationConstants.* ;

public class UpdateDateDto {
    @NotNull(message = ID_BLANK_MESSAGE)
    private Integer id;

    @NotBlank(message = DATE_BLANK_MESSAGE)
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
