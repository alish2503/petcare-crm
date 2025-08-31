package com.vet_care.demo.service;

import com.vet_care.demo.model.*;
import com.vet_care.demo.repository.AppointmentRepository;
import com.vet_care.demo.repository.AvailableSlotRepository;
import com.vet_care.demo.repository.DoctorRepository;
import com.vet_care.demo.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AvailableSlotRepository availableSlotRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private AppointmentService appointmentService;

    private Pet pet;
    private PetOwner user;
    private Doctor doctor;
    private AvailableSlot slot;

    @BeforeEach
    void setUp() {
        user = new PetOwner();
        user.setId(1L);

        pet = new Pet();
        pet.setId(10L);
        pet.setOwner(user);

        doctor = new Doctor();
        doctor.setId(100L);

        slot = new AvailableSlot();
        slot.setId(200L);
        slot.setSlotTime(LocalDateTime.now().plusDays(1));
        slot.setBooked(false);
    }

    @Test
    void addAppointment_ShouldCreateAppointment_WhenSlotAvailable() {
        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));
        when(doctorRepository.findById(100L)).thenReturn(Optional.of(doctor));
        when(availableSlotRepository.findById(200L)).thenReturn(Optional.of(slot));
        when(appointmentRepository.existsBySlotSlotTimeAndDoctor(any(), any())).thenReturn(false);

        Appointment saved = new Appointment("Checkup", pet, doctor, slot);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(saved);

        Appointment result = appointmentService.addAppointment("Checkup", 10L, 100L, 200L);

        assertNotNull(result);
        assertEquals("Checkup", result.getReason());
        assertTrue(slot.isBooked());

        verify(availableSlotRepository).save(slot);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void addAppointment_ShouldThrowException_WhenDoctorNotFound() {
        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));
        when(doctorRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> appointmentService.addAppointment("Checkup", 10L, 100L, 200L));
    }

    @Test
    void addAppointment_ShouldThrowException_WhenSlotNotFound() {
        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));
        when(doctorRepository.findById(100L)).thenReturn(Optional.of(doctor));
        when(availableSlotRepository.findById(200L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> appointmentService.addAppointment("Checkup", 10L, 100L, 200L));
    }

    @Test
    void addAppointment_ShouldThrowException_WhenAppointmentAlreadyExists() {
        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));
        when(doctorRepository.findById(100L)).thenReturn(Optional.of(doctor));
        when(availableSlotRepository.findById(200L)).thenReturn(Optional.of(slot));
        when(appointmentRepository.existsBySlotSlotTimeAndDoctor(any(), any())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> appointmentService.addAppointment("Checkup", 10L, 100L, 200L));
    }

    @Test
    void deleteAppointment_ShouldDelete_WhenUserIsOwner() {
        Appointment appointment = new Appointment("Checkup", pet, doctor, slot);
        appointment.setId(300L);

        when(appointmentRepository.findById(300L)).thenReturn(Optional.of(appointment));
        when(cacheManager.getCache("petsByDoctor")).thenReturn(cache);
        when(doctorRepository.findDoctorIdByAppointment(appointment)).thenReturn(100L);

        appointmentService.deleteAppointment(300L, user);

        assertFalse(slot.isBooked());
        verify(availableSlotRepository).save(slot);
        verify(appointmentRepository).delete(appointment);
        verify(cache).evict(100L);
    }

    @Test
    void deleteAppointment_ShouldThrowException_WhenAppointmentNotFound() {
        when(appointmentRepository.findById(300L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> appointmentService.deleteAppointment(300L, user));
    }

    @Test
    void deleteAppointment_ShouldThrowException_WhenUserNotOwner() {
        PetOwner otherUser = new PetOwner();
        otherUser.setId(999L);

        Appointment appointment = new Appointment("Checkup", pet, doctor, slot);
        appointment.setId(300L);

        when(appointmentRepository.findById(300L)).thenReturn(Optional.of(appointment));

        assertThrows(AccessDeniedException.class,
                () -> appointmentService.deleteAppointment(300L, otherUser));
    }
}
