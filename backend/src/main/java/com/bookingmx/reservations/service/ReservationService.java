package com.bookingmx.reservations.service;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.model.ReservationStatus;
import com.bookingmx.reservations.repo.ReservationRepository;
import com.bookingmx.reservations.exception.BadRequestException;
import com.bookingmx.reservations.exception.NotFoundException;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * ReservationService
 *
 * This service layer contains the business logic for handling reservations.
 * It validates data, enforces rules, interacts with the repository, and
 * ensures consistency before saving or updating reservation entities.
 */
@Service
public class ReservationService {

    /**
     * repo
     *
     * Repository used for performing CRUD operations on reservations.
     */
    private final ReservationRepository repo;

    /**
     * Constructor that injects the ReservationRepository dependency.
     *
     * @param repo The repository that stores Reservation objects.
     */
    public ReservationService(ReservationRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieves all reservations in the system.
     *
     * @return A list of all Reservation entities in the repository.
     */
    public List<Reservation> list() {
        return repo.findAll();
    }

    /**
     * Creates a new reservation.
     *
     * Steps performed:
     * - Validates check-in and check-out dates.
     * - Creates a new Reservation entity using the request data.
     * - Saves the reservation to the repository.
     *
     * @param req The input data needed to create a reservation.
     * @return The newly created Reservation object.
     */
    public Reservation create(ReservationRequest req) {
        validateDates(req.getCheckIn(), req.getCheckOut());
        Reservation r = new Reservation(null, req.getGuestName(), req.getHotelName(), req.getCheckIn(), req.getCheckOut());
        Reservation r = new Reservation(
                null,
                req.getGuestName(),
                req.getHotelName(),
                req.getCheckIn(),
                req.getCheckOut()
        );
        return repo.save(r);
    }

    /**
     * Updates an existing reservation.
     *
     * Steps performed:
     * - Finds the reservation by ID or throws NotFoundException.
     * - Ensures the reservation is active (cannot update canceled reservations).
     * - Validates the date range.
     * - Updates reservation fields.
     * - Saves the updated reservation.
     *
     * @param id  The reservation ID to update.
     * @param req The new reservation data.
     * @return The updated Reservation entity.
     */
    public Reservation update(Long id, ReservationRequest req) {
        Reservation existing = repo.findById(id).orElseThrow(() -> new NotFoundException("Reservation not found"));
        if (!existing.isActive()) throw new BadRequestException("Cannot update a canceled reservation");
        Reservation existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        if (!existing.isActive())
            throw new BadRequestException("Cannot update a canceled reservation");

        validateDates(req.getCheckIn(), req.getCheckOut());

        existing.setGuestName(req.getGuestName());
        existing.setHotelName(req.getHotelName());
        existing.setCheckIn(req.getCheckIn());
        existing.setCheckOut(req.getCheckOut());

        return repo.save(existing);
    }

    /**
     * Cancels an existing reservation by setting its status to CANCELED.
     *
     * @param id The ID of the reservation to cancel.
     * @return The canceled Reservation object after saving the change.
     */
    public Reservation cancel(Long id) {
        Reservation existing = repo.findById(id).orElseThrow(() -> new NotFoundException("Reservation not found"));
        Reservation existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        existing.setStatus(ReservationStatus.CANCELED);
        return repo.save(existing);
    }

    private void validateDates(LocalDate in, LocalDate out) {
        if (in == null || out == null) throw new BadRequestException("Dates cannot be null");
        if (!out.isAfter(in)) throw new BadRequestException("Check-out must be after check-in");
        if (in.isBefore(LocalDate.now())) throw new BadRequestException("Check-in must be in the future");
        if (out.isBefore(LocalDate.now())) throw new BadRequestException("Check-out must be in the future");
    }
}