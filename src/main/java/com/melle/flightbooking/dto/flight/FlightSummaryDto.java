package com.melle.flightbooking.dto.flight;

public record FlightSummaryDto(
        Integer id,
        String origin,
        String destination,
        String date,
        Integer seats
        ) {}
