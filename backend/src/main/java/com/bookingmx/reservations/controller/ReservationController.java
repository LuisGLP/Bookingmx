package com.bookingmx.reservations.controller;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.dto.ReservationResponse;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.service.ReservationService;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "*"})
@RequestMapping(value = "/api/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping(produces = "application/json")
    public List<ReservationResponse> list() {
        return service.list().stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ReservationResponse create(@Valid @RequestBody ReservationRequest req) {
        return toResponse(service.create(req));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReservationResponse update(@PathVariable("id") Long id, @Valid @RequestBody ReservationRequest req) {
        return toResponse(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ReservationResponse cancel(@PathVariable("id") Long id) {
        return toResponse(service.cancel(id));
    }

    private ReservationResponse toResponse(Reservation r) {
        return new ReservationResponse(
                r.getId(), r.getGuestName(), r.getHotelName(), r.getCheckIn(), r.getCheckOut(), r.getStatus()
        );
    }
}
