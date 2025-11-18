package com.bookingmx.reservations.repo;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.exception.BadRequestException;
import com.bookingmx.reservations.exception.NotFoundException;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.service.ReservationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * ReservationRepositoryTest
 *
 * This class performs two types of tests:
 *
 * 1. **Real Repository Tests**
 *    - Verifies the behavior of the manual in-memory implementation
 *      of ReservationRepository.
 *
 * 2. **Service Layer Tests with Mocked Repository**
 *    - Validates business logic inside ReservationService using Mockito mocks.
 *
 * These tests ensure that both the repository and service function correctly
 * in isolation.
 */
class ReservationRepositoryTest {

    // ---------- REAL REPOSITORY (used to test repository functionality) ----------
    /** Real instance of ReservationRepository for validating CRUD behavior. */
    private ReservationRepository realRepo;

    // ---------- MOCKED REPOSITORY (used to test the service layer) ----------
    /** Mocked repository used to isolate and test ReservationService logic. */
    private ReservationRepository mockRepo;

    /** Service layer instance that depends on the mocked repository. */
    private ReservationService service;

    /**
     * Setup executed before each test.
     *
     * Initializes:
     * - A real repository instance
     * - A mocked repository
     * - A service instance using the mock
     */
    @BeforeEach
    void setup() {
        realRepo = new ReservationRepository();
        mockRepo = Mockito.mock(ReservationRepository.class);
        service = new ReservationService(mockRepo);
    }


    // =====================================================
    //  TESTS FOR THE REAL REPOSITORY
    // =====================================================

    /**
     * Tests that save() assigns an ID to a new reservation
     * and correctly stores it.
     */
    @Test
    void testSaveCreatesIdAndStoresReservation() {
        Reservation r = new Reservation(null, "Luis", "Hotel A",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));

        Reservation saved = realRepo.save(r);

        assertNotNull(saved.getId());
        assertEquals("Luis", saved.getGuestName());
    }

    /**
     * Tests that findById() returns the correct stored reservation.
     */
    @Test
    void testFindByIdReturnsReservation() {
        Reservation r = realRepo.save(new Reservation(null, "Ana", "Hotel B",
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4)));

        Optional<Reservation> found = realRepo.findById(r.getId());

        assertTrue(found.isPresent());
        assertEquals("Ana", found.get().getGuestName());
    }

    /**
     * Tests that findAll() returns a non-empty list after inserting an entry.
     */
    @Test
    void testFindAllReturnsList() {
        realRepo.save(new Reservation(null, "Carlos", "Hotel C",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));

        List<Reservation> list = realRepo.findAll();

        assertFalse(list.isEmpty());
    }

    /**
     * Tests that delete() properly removes an existing reservation.
     */
    @Test
    void testDeleteRemovesReservation() {
        Reservation r = realRepo.save(new Reservation(null, "Maria", "Hotel D",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));

        realRepo.delete(r.getId());

        assertTrue(realRepo.findById(r.getId()).isEmpty());
    }


    // =====================================================
    //  TESTS FOR SERVICE LAYER WITH MOCKED REPOSITORY
    // =====================================================

    /**
     * Tests that list() returns the full list of reservations
     * returned by the mocked repository.
     */
    @Test
    void testListReturnsAllReservations() {
        when(mockRepo.findAll()).thenReturn(List.of(
                new Reservation(1L, "Luis", "Hotel A",
                        LocalDate.now().plusDays(1), LocalDate.now().plusDays(2))
        ));

        List<Reservation> result = service.list();

        assertEquals(1, result.size());
        assertEquals("Luis", result.get(0).getGuestName());
    }

    /**
     * Tests a successful update operation.
     *
     * Validates:
     * - The reservation exists
     * - The service updates fields correctly
     * - save() returns the updated reservation
     */
    @Test
    void testUpdateSuccess() {
        Reservation existing = new Reservation(1L, "Old", "Old Hotel",
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4));

        when(mockRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(mockRepo.save(any())).thenAnswer(inv -> inv.getArgument(0)); // return input object

        ReservationRequest req = new ReservationRequest("New", "New Hotel",
                LocalDate.now().plusDays(5), LocalDate.now().plusDays(6));

        Reservation updated = service.update(1L, req);

        assertEquals("New", updated.getGuestName());
        assertEquals("New Hotel", updated.getHotelName());
    }

    /**
     * Tests that update() throws NotFoundException when
     * attempting to modify a non-existent reservation.
     */
    @Test
    void testUpdateThrowsNotFound() {
        when(mockRepo.findById(99L)).thenReturn(Optional.empty());

        ReservationRequest req = new ReservationRequest("A", "B",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));

        assertThrows(NotFoundException.class,
                () -> service.update(99L, req));
    }

    /**
     * Tests that update() throws BadRequestException when
     * the request contains invalid date values.
     */
    @Test
    void testValidateDatesThrowsBadRequest() {
        Reservation existing = new Reservation(1L, "Luis", "Error Hotel",
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4));

        when(mockRepo.findById(1L)).thenReturn(Optional.of(existing));

        // Invalid date scenario: check-out equals check-in
        ReservationRequest req = new ReservationRequest("Luis", "Hotel",
                LocalDate.now().plusDays(5), LocalDate.now().plusDays(5));

        assertThrows(BadRequestException.class,
                () -> service.update(1L, req));
    }
}
