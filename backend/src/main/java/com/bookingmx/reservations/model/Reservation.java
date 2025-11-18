package com.bookingmx.reservations.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Reservation
 *
 * Represents the main domain model for a hotel reservation.
 * This class stores essential information such as guest name,
 * hotel name, reservation dates, and the current status.
 */
public class Reservation {

    /**
     * id
     *
     * Unique identifier for the reservation.
     * Used to differentiate one reservation from another.
     */
    private Long id;

    /**
     * guestName
     *
     * Name of the person who made the reservation.
     */
    private String guestName;

    /**
     * hotelName
     *
     * Name of the hotel where the reservation is made.
     */
    private String hotelName;

    /**
     * checkIn
     *
     * The check-in date for the reservation.
     */
    private LocalDate checkIn;

    /**
     * checkOut
     *
     * The check-out date for the reservation.
     */
    private LocalDate checkOut;

    /**
     * status
     *
     * Current reservation status (e.g., ACTIVE or CANCELED).
     * Defaults to ACTIVE upon creation.
     */
    private ReservationStatus status = ReservationStatus.ACTIVE;

    /**
     * Default constructor.
     *
     * Used by frameworks or libraries that require a no-argument constructor.
     */
    public Reservation() {}

    /**
     * Parameterized constructor.
     *
     * @param id        Unique reservation identifier.
     * @param guestName Name of the guest.
     * @param hotelName Name of the hotel.
     * @param checkIn   Check-in date.
     * @param checkOut  Check-out date.
     *
     * Status is automatically set to ACTIVE when using this constructor.
     */
    public Reservation(Long id, String guestName, String hotelName, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.guestName = guestName;
        this.hotelNa
