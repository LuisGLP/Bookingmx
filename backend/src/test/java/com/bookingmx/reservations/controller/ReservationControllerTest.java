package com.bookingmx.reservations.controller;

import com.bookingmx.reservations.controller.GlobalExceptionHandler;
import com.bookingmx.reservations.controller.ReservationController;
import com.bookingmx.reservations.exception.BadRequestException;
import com.bookingmx.reservations.exception.NotFoundException;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.model.ReservationStatus;
import com.bookingmx.reservations.service.ReservationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

/**
 * ReservationControllerTest
 *
 * This test class verifies the behavior of ReservationController endpoints.
 * It uses MockMvc to simulate HTTP requests and Mockito to mock
 * ReservationService interactions.
 *
 * @WebMvcTest loads only the web layer (controllers).
 * @Import registers GlobalExceptionHandler so exception mapping is tested.
 */

@WebMvcTest(ReservationController.class)
@Import(GlobalExceptionHandler.class)
class ReservationControllerTest {

    /**
     * mockMvc
     *
     * Allows performing mock HTTP requests against the controller.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * service
     *
     * Mocked ReservationService used to control responses and simulate behaviors
     * for each test case.
     */
    @MockBean
    private ReservationService service;

    /**
     * Helper method that creates a sample Reservation instance for testing.
     *
     * @param id The reservation ID.
     * @return A Reservation object with preset sample data.
     */
    private Reservation buildReservation(Long id) {
        return new Reservation(
                id,
                "Luis",
                "Hotel Azul",
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(5)
        );
    }

    /**
     * Helper method that builds a JSON string for POST/PUT request payload.
     *
     * @return JSON containing valid reservation request data.
     */
    private String buildRequestJson() {
        return """
                {
                    "guestName": "Luis",
                    "hotelName": "Hotel Azul",
                    "checkIn": "%s",
                    "checkOut": "%s"
                }
                """.formatted(
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(5)
        );
    }

    /**
     * Tests GET /api/reservations
     *
     * Ensures the controller returns a list of reservations
     * and properly serializes JSON.
     */
    @Test
    void testListReservations() throws Exception {
        when(service.list()).thenReturn(List.of(buildReservation(1L)));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].guestName", is("Luis")));
    }

    /**
     * Tests POST /api/reservations
     *
     * Ensures a reservation is created successfully and returned as JSON.
     */
    @Test
    void testCreateReservation() throws Exception {
        Reservation saved = buildReservation(1L);
        when(service.create(any())).thenReturn(saved);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildRequestJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.guestName", is("Luis")));
    }

    /**
     * Tests PUT /api/reservations/{id}
     *
     * Ensures updating a reservation returns correct updated data.
     */
    @Test
    void testUpdateReservation() throws Exception {
        Reservation updated = buildReservation(2L);
        when(service.update(eq(2L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/reservations/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildRequestJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)));
    }

    /**
     * Tests DELETE /api/reservations/{id}
     *
     * Ensures cancellation marks a reservation as CANCELED.
     */
    @Test
    void testCancelReservation() throws Exception {
        Reservation canceled = buildReservation(3L);
        canceled.setStatus(ReservationStatus.CANCELED);

        when(service.cancel(3L)).thenReturn(canceled);

        mockMvc.perform(delete("/api/reservations/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CANCELED")));
    }

    /**
     * Tests NotFoundException handling via GlobalExceptionHandler.
     */
    @Test
    void testHandleNotFound() throws Exception {
        when(service.cancel(99L)).thenThrow(new NotFoundException("Reservation not found"));

        mockMvc.perform(delete("/api/reservations/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reservation not found"));
    }

    /**
     * Tests BadRequestException handling via GlobalExceptionHandler.
     */
    @Test
    void testHandleBadRequest() throws Exception {
        when(service.create(any())).thenThrow(new BadRequestException("Invalid data"));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildRequestJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid data"));
    }

    /**
     * Tests generic exception handling (non-specific errors).
     */
    @Test
    void testHandleOtherException() throws Exception {
        when(service.list()).thenThrow(new RuntimeException("Boom"));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal error"));
    }
}