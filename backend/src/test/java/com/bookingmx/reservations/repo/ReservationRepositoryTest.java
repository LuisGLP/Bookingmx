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

class ReservationRepositoryTest {

    // ---------- REPO REAL (para probar funcionamiento del repositorio) ----------
    private ReservationRepository realRepo;

    // ---------- REPO MOCK (para probar service) ----------
    private ReservationRepository mockRepo;
    private ReservationService service;

    @BeforeEach
    void setup() {
        realRepo = new ReservationRepository();
        mockRepo = Mockito.mock(ReservationRepository.class);
        service = new ReservationService(mockRepo);
    }


    // =====================================================
    //  TESTS DEL REPOSITORIO REAL
    // =====================================================

    @Test
    void testSaveCreatesIdAndStoresReservation() {
        Reservation r = new Reservation(null, "Luis", "Hotel A",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));

        Reservation saved = realRepo.save(r);

        assertNotNull(saved.getId());
        assertEquals("Luis", saved.getGuestName());
    }

    @Test
    void testFindByIdReturnsReservation() {
        Reservation r = realRepo.save(new Reservation(null, "Ana", "Hotel B",
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4)));

        Optional<Reservation> found = realRepo.findById(r.getId());

        assertTrue(found.isPresent());
        assertEquals("Ana", found.get().getGuestName());
    }

    @Test
    void testFindAllReturnsList() {
        realRepo.save(new Reservation(null, "Carlos", "Hotel C",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));

        List<Reservation> list = realRepo.findAll();

        assertFalse(list.isEmpty());
    }

    @Test
    void testDeleteRemovesReservation() {
        Reservation r = realRepo.save(new Reservation(null, "Maria", "Hotel D",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));

        realRepo.delete(r.getId());

        assertTrue(realRepo.findById(r.getId()).isEmpty());
    }


    // =====================================================
    //  TESTS DEL SERVICE CON MOCKS
    // =====================================================

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

    @Test
    void testUpdateSuccess() {
        Reservation existing = new Reservation(1L, "Old", "Old Hotel",
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4));

        when(mockRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(mockRepo.save(any())).thenAnswer(inv -> inv.getArgument(0)); // lambda

        ReservationRequest req = new ReservationRequest("New", "New Hotel",
                LocalDate.now().plusDays(5), LocalDate.now().plusDays(6));

        Reservation updated = service.update(1L, req);

        assertEquals("New", updated.getGuestName());
        assertEquals("New Hotel", updated.getHotelName());
    }

    @Test
    void testUpdateThrowsNotFound() {
        when(mockRepo.findById(99L)).thenReturn(Optional.empty());

        ReservationRequest req = new ReservationRequest("A", "B",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));

        assertThrows(NotFoundException.class,
                () -> service.update(99L, req));
    }

    @Test
    void testValidateDatesThrowsBadRequest() {
        Reservation existing = new Reservation(1L, "Luis", "Error Hotel",
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4));

        when(mockRepo.findById(1L)).thenReturn(Optional.of(existing));

        ReservationRequest req = new ReservationRequest("Luis", "Hotel",
                LocalDate.now().plusDays(5), LocalDate.now().plusDays(5)); // fechas inválidas

        assertThrows(BadRequestException.class,
                () -> service.update(1L, req));
    }
}
