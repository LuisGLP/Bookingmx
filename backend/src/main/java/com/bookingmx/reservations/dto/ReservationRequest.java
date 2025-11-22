package com.bookingmx.reservations.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * ReservationRequest
 *
 * This DTO represents the data required to create or update a reservation.
 * It is used as the request body in the API endpoints. Validation annotations
 * ensure that the incoming data follows the expected format and business rules.
 */

public class ReservationRequest {

    /**
     * guestName
     *
     * Represents the full name of the guest making the reservation.
     * - @NotBlank ensures the field cannot be null or an empty string.
     */

    @NotBlank
    private String guestName;

    /**
     * hotelName
     *
     * Indicates the name of the hotel where the reservation is being made.
     * - @NotBlank ensures the field contains text.
     */

    @NotBlank
    private String hotelName;

    /**
     * checkIn
     *
     * Check-in date for the reservation.
     * - @NotNull ensures the date is provided.
     * - @Future ensures the date is strictly in the future.
     */

    @NotNull @Future
    private LocalDate checkIn;

    /**
     * checkOut
     *
     * Check-out date for the reservation.
     * - @NotNull ensures the date is provided.
     * - @Future ensures the date is strictly in the future.
     */

    @NotNull @Future
    private LocalDate checkOut;

    /**
     * Constructor with all fields.
     *
     * @param guestName The guest's name.
     * @param hotelName The hotel's name.
     * @param checkIn   The check-in date.
     * @param checkOut  The check-out date.
     *
     * This constructor is typically used when creating a new reservation request programmatically.
     */

    public ReservationRequest(String guestName, String hotelName, LocalDate checkIn, LocalDate checkOut) {
        // ===================== Getters & Setters =====================
        this.guestName = guestName;
        this.hotelName = hotelName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
    public ReservationRequest() {}

    /** @return guestName The guest's full name. */
    public String getGuestName() { return guestName; }
    /** @param guestName Sets the guest's name. */
    public void setGuestName(String guestName) { this.guestName = guestName; }
    /** @return hotelName The name of the hotel. */
    public String getHotelName() { return hotelName; }
    /** @param hotelName Sets the hotel name. */
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    /** @return checkIn The check-in date. */
    public LocalDate getCheckIn() { return checkIn; }
    /** @param checkIn Sets the check-in date. */
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    /** @return checkOut The check-out date. */
    public LocalDate getCheckOut() { return checkOut; }
    /** @param checkOut Sets the check-out date. */
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

}
