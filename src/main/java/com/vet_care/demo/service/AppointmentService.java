package com.vet_care.demo.service;

import com.vet_care.demo.dto.AppointmentsPageProjection;
import com.vet_care.demo.model.*;
import com.vet_care.demo.repository.AppointmentRepository;
import com.vet_care.demo.repository.AvailableSlotRepository;
import com.vet_care.demo.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */

@Service
public class AppointmentService {

    private final PetService petService;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AvailableSlotRepository availableSlotRepository;

    public AppointmentService(PetService petService,
                              AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              AvailableSlotRepository availableSlotRepository) {

        this.petService = petService;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.availableSlotRepository = availableSlotRepository;
    }

    @Transactional
    public Appointment addAppointment(String reason, Long petId, Long doctorId, Long slotId) {

        Pet pet = petService.getPetById(petId);
        Doctor doctor = doctorRepository.findById(doctorId).
                orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        AvailableSlot slot = availableSlotRepository.
                findById(slotId).orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        LocalDateTime slotDateTime = slot.getDateTime();
        boolean alreadyExists = appointmentRepository.existsBySlot_DateTimeAndDoctor(slotDateTime, doctor);
        if (alreadyExists) {
            throw new IllegalStateException("Appointment already exists for this slot and doctor");
        }
        slot.setBooked(true);
        availableSlotRepository.save(slot);
        return appointmentRepository.save(new Appointment(reason, pet, doctor, slot));
    }

    public List<AppointmentsPageProjection> getAppointmentsForUser(PetOwner user) {
        return appointmentRepository.findUpcomingAppointments(user, LocalDateTime.now());
    }

    @Transactional
    public void deleteAppointment(Long appointmentId, PetOwner user) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        PetOwner owner = appointment.getPet().getOwner();
        if (!owner.getId().equals(user.getId())) {
            throw new SecurityException("You are not allowed to delete this appointment");
        }
        AvailableSlot slot = appointment.getSlot();
        slot.setBooked(false);
        availableSlotRepository.save(slot);
        appointmentRepository.delete(appointment);
    }
}

