package com.bookingmx.reservations.repo;

import com.bookingmx.reservations.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ReservationRepository
 *
 * This repository simulates a persistence layer for Reservation objects.
 * It uses an in-memory ConcurrentHashMap as storage, making it thread-safe
 * and suitable for simple/demo applications or local testing.
 *
 * The repository provides CRUD operations such as:
 * - Creating and updating reservations
 * - Retrieving all reservations or by ID
 * - Deleting reservations
 *
 * IDs are automatically generated using an AtomicLong sequence.
 */
@Repository
public class ReservationRepository {

    /**
     * store
     *
     * Thread-safe in-memory storage for Reservation entities.
     * Keys represent reservation IDs and values represent reservation objects.
     */
    private final Map<Long, Reservation> store = new ConcurrentHashMap<>();

    /**
     * seq
     *
     * Atomic counter used for generating unique reservation IDs.
     * Starts at 1 and increments for each new reservation.
     */
    private final AtomicLong seq = new AtomicLong(1L);

    /**
     * Retrieves all reservations stored in memory.
     *
     * @return A list containing all Reservation objects.
     *         A new list is returned to avoid exposing internal state.
     */
    public List<Reservation> findAll() {
        return new ArrayList<>(store.values());
    }

    /**
     * Searches for a reservation by its ID.
     *
     * @param id The ID of the reservation to find.
     * @return An Optional containing the reservation if found,
     *         or empty if the ID does not exist.
     */
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Saves a reservation into the repository.
     *
     * If the reservation has no ID, one is automatically assigned.
     * If the ID exists, the reservation is updated.
     *
     * @param r The reservation to save.
     * @return The saved reservation with its assigned ID.
     */
    public Reservation save(Reservation r) {
        if (r.getId() == null) r.setId(seq.getAndIncrement());
        store.put(r.getId(), r);
        return r;
    }

    /**
     * Deletes a reservation from the repository by its ID.
     *
     * @param id The ID of the reservation to remove.
     */
    public void delete(Long id) {
        store.remove(id);
    }
}
