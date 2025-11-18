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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public boolean isActive() { return this.status == ReservationStatus.ACTIVE; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override public int hashCode() { return Objects.hash(id); }
}