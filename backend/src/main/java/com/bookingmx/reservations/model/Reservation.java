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
        this.hotelName = hotelName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = ReservationStatus.ACTIVE;
    }

    /** @return The reservation ID. */
    public Long getId() { return id; }

    /** Sets the reservation ID. */
    public void setId(Long id) { this.id = id; }

    /** @return The guest's name. */
    public String getGuestName() { return guestName; }

    /** Sets the guest's name. */
    public void setGuestName(String guestName) { this.guestName = guestName; }

    /** @return The hotel's name. */
    public String getHotelName() { return hotelName; }

    /** Sets the hotel's name. */
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    /** @return The check-in date. */
    public LocalDate getCheckIn() { return checkIn; }

    /** Sets the check-in date. */
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    /** @return The check-out date. */
    public LocalDate getCheckOut() { return checkOut; }

    /** Sets the check-out date. */
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

    /** @return The current reservation status. */
    public ReservationStatus getStatus() { return status; }

    /** Updates the reservation status. */
    public void setStatus(ReservationStatus status) { this.status = status; }

    /**
     * Checks if the reservation is currently active.
     *
     * @return true if status is ACTIVE, false otherwise.
     */
    public boolean isActive() { return this.status == ReservationStatus.ACTIVE; }


    /**
     * equals method
     *
     * Two reservations are considered equal if they share the same ID.
     * This allows proper comparison when using data structures such as sets.
     */
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    /**
     * hashCode method
     *
     * Generates a hash based solely on the reservation ID.
     * Ensures consistency with the equals() implementation.
     */

    @Override public int hashCode() { return Objects.hash(id); }
}