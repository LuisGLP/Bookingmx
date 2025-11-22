package com.bookingmx.reservations.controller;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.dto.ReservationResponse;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.service.ReservationService;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ReservationController
 *
 * This class exposes REST endpoints for managing reservations.
 * It handles operations such as listing, creating, updating and canceling reservations.
 * All responses are produced in JSON format.
 */

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "*"})
@RequestMapping(value = "/api/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    private final ReservationService service;

    /**
     * Constructor-based dependency injection for ReservationService.
     *
     * @param service The service layer that contains business logic for reservations.
     */

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    /**
     * GET /api/reservations
     *
     * Retrieves all reservations.
     *
     * @return A list of ReservationResponse objects.
     *
     * This method calls service.list() and maps each Reservation entity
     * into a ReservationResponse DTO.
     */

    @GetMapping(produces = "application/json")
    public List<ReservationResponse> list() {
        return service.list().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * POST /api/reservations
     *
     * Creates a new reservation.
     *
     * @param req The reservation request body, validated with @Valid.
     * @return ReservationResponse containing the created reservation.
     *
     * The request is passed to the service layer, and the result is converted
     * into a ReservationResponse DTO.
     */

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ReservationResponse create(@Valid @RequestBody ReservationRequest req) {
        return toResponse(service.create(req));
    }

    /**
     * PUT /api/reservations/{id}
     *
     * Updates an existing reservation by ID.
     *
     * @param id  The ID of the reservation to update.
     * @param req The updated reservation data, validated with @Valid.
     * @return ReservationResponse with updated reservation information.
     */

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReservationResponse update(@PathVariable("id") Long id, @Valid @RequestBody ReservationRequest req) {
        return toResponse(service.update(id, req));
    }

    /**
     * DELETE /api/reservations/{id}
     *
     * Cancels (deletes logically) a reservation by ID.
     *
     * @param id The ID of the reservation to cancel.
     * @return ReservationResponse representing the canceled reservation.
     */

    @DeleteMapping("/{id}")
    public ReservationResponse cancel(@PathVariable("id") Long id) {
        return toResponse(service.cancel(id));
    }

    /**
     * Converts a Reservation entity into a ReservationResponse DTO.
     *
     * @param r The Reservation entity.
     * @return A new ReservationResponse containing the mapped fields.
     *
     * This method extracts and passes individual Reservation attributes
     * into the corresponding DTO constructor.
     */

    private ReservationResponse toResponse(Reservation r) {
        return new ReservationResponse(
                r.getId(), r.getGuestName(), r.getHotelName(), r.getCheckIn(), r.getCheckOut(), r.getStatus()
        );
    }
}
package com.bookingmx.reservations.controller;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.dto.ReservationResponse;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.service.ReservationService;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ReservationController
 *
 * This class exposes REST endpoints for managing reservations.
 * It handles operations such as listing, creating, updating and canceling reservations.
 * All responses are produced in JSON format.
 */
@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "*"})
@RequestMapping(value = "/api/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    private final ReservationService service;

    /**
     * Constructor-based dependency injection for ReservationService.
     *
     * @param service The service layer that contains business logic for reservations.
     */
    public ReservationController(ReservationService service) {
        this.service = service;
    }

    /**
     * GET /api/reservations
     *
     * Retrieves all reservations.
     *
     * @return A list of ReservationResponse objects.
     *
     * This method calls service.list() and maps each Reservation entity
     * into a ReservationResponse DTO.
     */
    @GetMapping(produces = "application/json")
    public List<ReservationResponse> list() {
        return service.list().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * POST /api/reservations
     *
     * Creates a new reservation.
     *
     * @param req The reservation request body, validated with @Valid.
     * @return ReservationResponse containing the created reservation.
     *
     * The request is passed to the service layer, and the result is converted
     * into a ReservationResponse DTO.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ReservationResponse create(@Valid @RequestBody ReservationRequest req) {
        return toResponse(service.create(req));
    }

    /**
     * PUT /api/reservations/{id}
     *
     * Updates an existing reservation by ID.
     *
     * @param id  The ID of the reservation to update.
     * @param req The updated reservation data, validated with @Valid.
     * @return ReservationResponse with updated reservation information.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReservationResponse update(@PathVariable("id") Long id, @Valid @RequestBody ReservationRequest req) {
        return toResponse(service.update(id, req));
    }

    /**
     * DELETE /api/reservations/{id}
     *
     * Cancels (deletes logically) a reservation by ID.
     *
     * @param id The ID of the reservation to cancel.
     * @return ReservationResponse representing the canceled reservation.
     */
    @DeleteMapping("/{id}")
    public ReservationResponse cancel(@PathVariable("id") Long id) {
        return toResponse(service.cancel(id));
    }

    /**
     * Converts a Reservation entity into a ReservationResponse DTO.
     *
     * @param r The Reservation entity.
     * @return A new ReservationResponse containing the mapped fields.
     *
     * This method extracts and passes individual Reservation attributes
     * into the corresponding DTO constructor.
     */
    private ReservationResponse toResponse(Reservation r) {
        return new ReservationResponse(
                r.getId(), r.getGuestName(), r.getHotelName(), r.getCheckIn(), r.getCheckOut(), r.getStatus()
        );
    }
}
