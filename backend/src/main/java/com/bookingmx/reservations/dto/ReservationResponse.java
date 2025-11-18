package com.bookingmx.reservations.dto;

import com.bookingmx.reservations.model.ReservationStatus;
import java.time.LocalDate;

/**
 * ReservationResponse
 *
 * This DTO is used for sending reservation information back to the client.
 * It represents the structured output returned by the API, containing
 * essential reservation details including status and dates.
 */
public class ReservationResponse {

    /**
     * id
     *
     * Unique identifier of the reservation.
     */
    private Long id;

    /**
     * guestName
     *
     * Name of the guest who made the reservation.
     */
    private String guestName;

    /**
     * hotelName
     *
     * Name of the hotel where the reservation is booked.
     */
    private String hotelName;

    /**
     * checkIn
     *
     * Check-in date of the reservation.
     */
    private LocalDate checkIn;

    /**
     * checkOut
     *
     * Check-out date of the reservation.
     */
    private LocalDate checkOut;

    /**
     * status
     *
     * Current status of the reservation (e.g., ACTIVE, CANCELED).
     * Defined in the ReservationStatus enum.
     */
    private ReservationStatus status;

    /**
     * Constructor used to create a new ReservationResponse object.
     *
     * @param id         Reservation identifier.
     * @param guestName  Guest's name.
     * @param hotelName  Hotel name.
     * @param checkIn    Reservation check-in date.
     * @param checkOut   Reservation check-out date.
     * @param status     Current reservation status.
     *
     * This constructor is used whenever a Reservation entity
     * is converted into a response DTO.
     */
    public ReservationResponse(Long id, String guestName, String hotelName, LocalDate checkIn, LocalDate checkOut, ReservationStatus status) {
        this.id = id; 
        this.guestName = guestName; 
        this.hotelName = hotelName;
        this.checkIn = checkIn; 
        this.checkOut = checkOut; 
        this.status = status;
    }

    /** @return The reservation ID. */
    public Long getId() { return id; }

    /** @return The guest's name. */
    public String getGuestName() { return guestName; }

    /** @return The hotel's name. */
    public String getHotelName() { return hotelName; }

    /** @return The check-in date. */
    public LocalDate getCheckIn() { return checkIn; }

    /** @return The check-out date. */
    public LocalDate getCheckOut() { return checkOut; }

    /** @return The reservation status. */
    public ReservationStatus getStatus() { return status; }
}
