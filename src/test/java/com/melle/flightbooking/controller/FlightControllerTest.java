package com.melle.flightbooking.controller;

import com.melle.flightbooking.service.FlightServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FlightController.class)
public class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightServiceImp flightServiceImp;

    @Test
    void createFlight_returnsCreatedFlight() throws Exception {

    }
}
