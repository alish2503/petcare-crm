package com.vet_care.demo.service;

import com.vet_care.demo.dto.DoctorDto;
import com.vet_care.demo.dto.FlatDoctorSlotDto;
import com.vet_care.demo.model.Specialization;
import com.vet_care.demo.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
public class AvailableDoctorsServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AvailableDoctorsService availableDoctorsService;

    @Test
    void getDoctorsWithAvailableFutureSlots_ShouldGroupSlotsAndPreserveSlotFields() {

        FlatDoctorSlotDto slot1 = new FlatDoctorSlotDto(1L, "Smith", Specialization.SURGERY,
                101L, LocalDateTime.now().minusDays(1), false);

        FlatDoctorSlotDto slot2 = new FlatDoctorSlotDto(1L, "Smith", Specialization.SURGERY,
                102L, LocalDateTime.now().plusDays(2), true);

        FlatDoctorSlotDto slot3 = new FlatDoctorSlotDto(2L, "Brown", Specialization.CARDIOLOGY,
                201L, LocalDateTime.now().plusDays(3), false);

        when(doctorRepository.findAllFutureDoctorSlotsFlat(any()))
                .thenReturn(List.of(slot2, slot3));

        List<DoctorDto> result = availableDoctorsService.getDoctorsWithAvailableFutureSlots();

        assertEquals(2, result.size());

        DoctorDto doctor1 = result.stream().filter(d -> d.getId() == 1L).findFirst().get();
        assertEquals("Smith", doctor1.getLastName());
        assertEquals(Specialization.SURGERY.name(), doctor1.getSpecialization());
        assertEquals(1, doctor1.getAvailableSlots().size());

        DoctorDto.SlotDTO slot2Doctor1 = doctor1.getAvailableSlots().get(0);
        assertEquals(102L, slot2Doctor1.getId());
        assertEquals(slot2.dateTime(), slot2Doctor1.getSlotTime());
        assertTrue(slot2Doctor1.isBooked());

        DoctorDto doctor2 = result.stream().filter(d -> d.getId() == 2L).findFirst().get();
        assertEquals("Brown", doctor2.getLastName());
        assertEquals(Specialization.CARDIOLOGY.name(), doctor2.getSpecialization());
        assertEquals(1, doctor2.getAvailableSlots().size());

        DoctorDto.SlotDTO slotDoctor2 = doctor2.getAvailableSlots().get(0);
        assertEquals(201L, slotDoctor2.getId());
        assertEquals(slot3.dateTime(), slotDoctor2.getSlotTime());
        assertFalse(slotDoctor2.isBooked());

        verify(doctorRepository).findAllFutureDoctorSlotsFlat(any());
    }
}
