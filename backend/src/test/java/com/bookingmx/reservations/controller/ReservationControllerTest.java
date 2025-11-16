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

@WebMvcTest(ReservationController.class)
@Import(GlobalExceptionHandler.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService service;

    private Reservation buildReservation(Long id) {
        return new Reservation(
                id,
                "Luis",
                "Hotel Azul",
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(5)
        );
    }

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

    @Test
    void testListReservations() throws Exception {
        when(service.list()).thenReturn(List.of(buildReservation(1L)));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].guestName", is("Luis")));
    }

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

    @Test
    void testCancelReservation() throws Exception {
        Reservation canceled = buildReservation(3L);
        canceled.setStatus(ReservationStatus.CANCELED);

        when(service.cancel(3L)).thenReturn(canceled);

        mockMvc.perform(delete("/api/reservations/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CANCELED")));
    }

    @Test
    void testHandleNotFound() throws Exception {
        when(service.cancel(99L)).thenThrow(new NotFoundException("Reservation not found"));

        mockMvc.perform(delete("/api/reservations/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reservation not found"));
    }

    @Test
    void testHandleBadRequest() throws Exception {
        when(service.create(any())).thenThrow(new BadRequestException("Invalid data"));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildRequestJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid data"));
    }

    @Test
    void testHandleOtherException() throws Exception {
        when(service.list()).thenThrow(new RuntimeException("Boom"));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal error"));
    }
}
