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

/**
 * ReservationServiceTest
 *
 * Unit test suite for the ReservationService class.
 * This class validates:
 *
 *  - Reservation creation behavior
 *  - Date validation logic
 *  - Cancellation workflow
 *  - Correct interaction with ReservationRepository using mocks
 *  - Persistence of expected values
 *  - Integrity of the Reservation model
 *
 * Mockito is used to isolate the service layer from the repository.
 */
public class ReservationServiceTest {

    /** Mocked repository to avoid touching real persistence. */
    @Mock
    private ReservationRepository repository;

    /** Service instance with mocks injected automatically. */
    @InjectMocks
    private ReservationService service;

    /**
     * Initializes Mockito mocks before each test case.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Helper method to build a valid ReservationRequest.
     * Used in multiple test cases to reduce duplication.
     */
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

    /**
     * Tests successful reservation creation.
     * Ensures the repository save() method is invoked
     * and the returned object contains expected data.
     */
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

    /**
     * Tests successful cancellation of a reservation.
     * Ensures status is updated to CANCELED and persisted.
     */
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

    /**
     * Tests cancellation failure when reservation ID does not exist.
     * Ensures NotFoundException is thrown and save() is never called.
     */
    @Test
    void cancelReservation_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(NotFoundException.class, () -> service.cancel(1L));

        assertTrue(ex.getMessage().contains("not found"));
        verify(repository, never()).save(any());
    }

    /**
     * Tests creation failure when check-out date is before check-in date.
     */
    @Test
    void testCreateReservation_InvalidDates() {

        ReservationRequest req = new ReservationRequest();
        req.setGuestName("Luis");
        req.setHotelName("Hotel Azul");
        req.setCheckIn(LocalDate.now().plusDays(5));
        req.setCheckOut(LocalDate.now().plusDays(3)); // invalid range

        Exception ex = assertThrows(
                BadRequestException.class,
                () -> service.create(req)
        );

        assertTrue(ex.getMessage().contains("Check-out must be after check-in"));
    }

    /**
     * Ensures that when a reservation is created,
     * the service correctly passes the expected values
     * to the repository.
     */
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

    /**
     * Tests validation for same-day check-in and check-out.
     * The service must reject this case.
     */
    @Test
    void testCreateReservation_SameDay_Invalid() {
        ReservationRequest req = new ReservationRequest();
        req.setGuestName("Luis");
        req.setHotelName("Hotel Azul");
        LocalDate day = LocalDate.now().plusDays(4);
        req.setCheckIn(day);
        req.setCheckOut(day); // invalid: same day

        Exception ex = assertThrows(BadRequestException.class, () -> service.create(req));
        assertTrue(ex.getMessage().toLowerCase().contains("after"));
        verify(repository, never()).save(any());
    }

    /**
     * Ensures that canceling a reservation results in:
     *  - Status being updated to CANCELED
     *  - save() being invoked with correct updated object
     */
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

    /**
     * Tests that the Reservation model behaves correctly
     * when using setters and getters.
     */
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
