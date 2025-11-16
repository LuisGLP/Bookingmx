package com.bookingmx.reservations.service;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.model.ReservationStatus;
import com.bookingmx.reservations.repo.ReservationRepository;
import com.bookingmx.reservations.exception.BadRequestException;
import com.bookingmx.reservations.exception.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;

    @InjectMocks
    private ReservationService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private ReservationRequest buildRequest() {
        ReservationRequest req = new ReservationRequest();
        req.setGuestName("Luis");
        req.setHotelName("Hotel Azul");
        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = checkIn.plusDays(2);
        req.setCheckIn(checkIn);
        req.setCheckOut(checkOut);
        return req;
    }

    @Test
    void testCreateReservation_Success() {

        ReservationRequest req = buildRequest();

        Reservation saved = new Reservation(
                1L,
                req.getGuestName(),
                req.getHotelName(),
                req.getCheckIn(),
                req.getCheckOut()
        );

        when(repository.save(any(Reservation.class))).thenReturn(saved);

        Reservation result = service.create(req);

        assertNotNull(result);
        assertEquals(req.getGuestName(), result.getGuestName());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testCancelReservation_Success() {

        Reservation res = new Reservation(
                1L,
                "Ana",
                "Hotel Verde",
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(5)
        );

        when(repository.findById(1L)).thenReturn(Optional.of(res));
        when(repository.save(res)).thenReturn(res);

        Reservation result = service.cancel(1L);

        assertEquals(ReservationStatus.CANCELED, result.getStatus());
        verify(repository).save(res);
    }

    @Test
    void cancelReservation_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(NotFoundException.class, () -> service.cancel(1L));

        assertTrue(ex.getMessage().contains("not found"));
        verify(repository, never()).save(any());
    }

    @Test
    void testCreateReservation_InvalidDates() {

        ReservationRequest req = new ReservationRequest();
        req.setGuestName("Luis");
        req.setHotelName("Hotel Azul");
        req.setCheckIn(LocalDate.now().plusDays(5));
        req.setCheckOut(LocalDate.now().plusDays(3)); // inválido

        Exception ex = assertThrows(
                BadRequestException.class,
                () -> service.create(req)
        );

        assertTrue(ex.getMessage().contains("Check-out must be after check-in"));
    }

    @Test
    void testCreateReservation_PersistsExpectedValues() {
        ReservationRequest req = buildRequest();

        when(repository.save(any(Reservation.class))).thenAnswer(inv -> {
            Reservation r = inv.getArgument(0);
            return new Reservation(1L, r.getGuestName(), r.getHotelName(), r.getCheckIn(), r.getCheckOut());
        });

        Reservation result = service.create(req);
        assertNotNull(result);

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(repository).save(captor.capture());

        Reservation toPersist = captor.getValue();
        assertEquals(req.getGuestName(), toPersist.getGuestName());
        assertEquals(req.getHotelName(), toPersist.getHotelName());
        assertEquals(req.getCheckIn(), toPersist.getCheckIn());
        assertEquals(req.getCheckOut(), toPersist.getCheckOut());
    }

    @Test
    void testCreateReservation_SameDay_Invalid() {
        ReservationRequest req = new ReservationRequest();
        req.setGuestName("Luis");
        req.setHotelName("Hotel Azul");
        LocalDate day = LocalDate.now().plusDays(4);
        req.setCheckIn(day);
        req.setCheckOut(day); // same day should be invalid

        Exception ex = assertThrows(BadRequestException.class, () -> service.create(req));
        assertTrue(ex.getMessage().toLowerCase().contains("after")); // message like "Check-out must be after check-in"
        verify(repository, never()).save(any());
    }

    @Test
    void testCancelReservation_SavesCanceledStatus() {
        Reservation res = new Reservation(
                2L,
                "Marta",
                "Hotel Rojo",
                LocalDate.now().plusDays(6),
                LocalDate.now().plusDays(8)
        );

        when(repository.findById(2L)).thenReturn(Optional.of(res));
        when(repository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        Reservation result = service.cancel(2L);

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(repository).save(captor.capture());
        Reservation saved = captor.getValue();

        assertEquals(ReservationStatus.CANCELED, saved.getStatus());
        assertEquals(ReservationStatus.CANCELED, result.getStatus());
    }

    @Test
    void testReservationModel() {
        Reservation r = new Reservation();
        r.setId(1L);
        r.setGuestName("Luis");
        r.setHotelName("Hotel Azul");
        r.setCheckIn(LocalDate.now().plusDays(1));
        r.setCheckOut(LocalDate.now().plusDays(2));
        r.setStatus(ReservationStatus.ACTIVE);

        assertEquals(1L, r.getId());
        assertEquals("Luis", r.getGuestName());
        assertTrue(r.isActive());
    }

}
