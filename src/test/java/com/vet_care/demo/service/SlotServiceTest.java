package com.vet_care.demo.service;

import com.vet_care.demo.model.AvailableSlot;
import com.vet_care.demo.model.Doctor;
import com.vet_care.demo.repository.AvailableSlotRepository;
import com.vet_care.demo.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class SlotServiceTest {

    @Mock
    private AvailableSlotRepository slotRepository;

    @InjectMocks
    private SlotService slotService;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);

        CustomUserDetails userDetails = new CustomUserDetails(doctor);
        Authentication auth = new TestingAuthenticationToken(userDetails, null);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);

        SecurityContextHolder.setContext(context);
    }

    @Test
    void getSlotsForCurrentDoctor_ShouldReturnSlots() {
        AvailableSlot slot1 = new AvailableSlot(LocalDateTime.now(), doctor);
        AvailableSlot slot2 = new AvailableSlot(LocalDateTime.now().plusHours(1), doctor);
        when(slotRepository.findByDoctorOrderBySlotTimeAsc(doctor))
                .thenReturn(List.of(slot1, slot2));

        List<AvailableSlot> result = slotService.getSlotsForCurrentDoctor();

        assertEquals(2, result.size());
        verify(slotRepository).findByDoctorOrderBySlotTimeAsc(doctor);
    }

    @Test
    void addSlotsForCurrentDoctor_ShouldReturnAndSaveSlots() {
        String dateTime = "2025-09-01 10:00";
        LocalDateTime expectedTime = LocalDateTime.of(2025, 9, 1, 10, 0);

        when(slotRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<AvailableSlot> result = slotService.addSlotsForCurrentDoctor(List.of(dateTime));

        assertEquals(1, result.size());
        assertEquals(expectedTime, result.get(0).getSlotTime());
        assertEquals(doctor, result.get(0).getDoctor());

        verify(slotRepository).saveAll(anyList());
    }


    @Test
    void deleteSlotForCurrentDoctor_ShouldDelete_WhenOwnerMatches() {
        AvailableSlot slot = new AvailableSlot(LocalDateTime.now(), doctor);
        slot.setId(100L);
        when(slotRepository.findById(100L)).thenReturn(Optional.of(slot));

        slotService.deleteSlotForCurrentDoctor(100L);

        verify(slotRepository).delete(slot);
    }

    @Test
    void deleteSlotForCurrentDoctor_ShouldThrow_WhenSlotNotFound() {
        when(slotRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> slotService.deleteSlotForCurrentDoctor(999L));
    }

    @Test
    void deleteSlotForCurrentDoctor_ShouldThrow_WhenDoctorDoesNotOwnSlot() {
        Doctor otherDoctor = new Doctor();
        otherDoctor.setId(2L);

        AvailableSlot slot = new AvailableSlot(LocalDateTime.now(), otherDoctor);
        slot.setId(200L);

        when(slotRepository.findById(200L)).thenReturn(Optional.of(slot));

        assertThrows(AccessDeniedException.class,
                () -> slotService.deleteSlotForCurrentDoctor(200L));
    }
}

