package com.bookingmx.reservations.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReservationRequest {
    @NotBlank
    private String guestName;
    @NotBlank
    private String hotelName;
    @NotNull @Future
    private LocalDate checkIn;
    @NotNull @Future
    private LocalDate checkOut;

    public ReservationRequest(String guestName, String hotelName, LocalDate checkIn, LocalDate checkOut) {
        this.guestName = guestName;
        this.hotelName = hotelName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
    public ReservationRequest() {}

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

}
