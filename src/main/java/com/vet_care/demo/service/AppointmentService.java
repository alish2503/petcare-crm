package com.vet_care.demo.service;

import com.vet_care.demo.dto.AppointmentsPageProjection;
import com.vet_care.demo.model.*;
import com.vet_care.demo.repository.AppointmentRepository;
import com.vet_care.demo.repository.AvailableSlotRepository;
import com.vet_care.demo.repository.DoctorRepository;
import com.vet_care.demo.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */

@Service
public class AppointmentService {

    private final PetRepository petRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AvailableSlotRepository availableSlotRepository;
    private final CacheManager cacheManager;

    public AppointmentService(PetRepository petRepository, AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              AvailableSlotRepository availableSlotRepository, CacheManager cacheManager) {

        this.petRepository = petRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.availableSlotRepository = availableSlotRepository;
        this.cacheManager = cacheManager;
    }


    @Transactional
    @CacheEvict(value = "petsByDoctor", key = "#doctorId")
    public Appointment addAppointment(String reason, Long petId, Long doctorId, Long slotId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        Doctor doctor = doctorRepository.findById(doctorId).
                orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        AvailableSlot slot = availableSlotRepository.
                findById(slotId).orElseThrow(() -> new EntityNotFoundException("Slot not found"));

        LocalDateTime slotDateTime = slot.getSlotTime();
        boolean alreadyExists = appointmentRepository.existsBySlotSlotTimeAndDoctor(slotDateTime, doctor);
        if (alreadyExists) {
            throw new IllegalArgumentException("Appointment already exists for this slot and doctor");
        }
        slot.setBooked(true);
        availableSlotRepository.save(slot);
        return appointmentRepository.save(new Appointment(reason, pet, doctor, slot));
    }

    public List<AppointmentsPageProjection> getAppointments(AbstractUser user) {
        if (user.getRole() == Role.ROLE_DOCTOR) {
            return appointmentRepository.findAppointmentsForDoctor((Doctor)user);
        }
        return appointmentRepository.findAppointmentsForOwner((PetOwner)user);
    }

    @Transactional
    public void deleteAppointment(Long appointmentId, PetOwner user) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        PetOwner owner = appointment.getPet().getOwner();
        if (!owner.getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this appointment");
        }
        AvailableSlot slot = appointment.getSlot();
        slot.setBooked(false);
        availableSlotRepository.save(slot);
        Cache cache = cacheManager.getCache("petsByDoctor");
        if (cache != null) {
            Long doctorId = doctorRepository.findDoctorIdByAppointment(appointment);
            cache.evict(doctorId);
        }
        appointmentRepository.delete(appointment);
    }
}

