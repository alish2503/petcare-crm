package com.vet_care.demo.repository;

import com.vet_care.demo.model.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alish
 */
@DataJpaTest
public class AvailableSlotRepositoryTest {

    @Autowired
    private AvailableSlotRepository slotRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetOwnerRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void resetSlotsForPet_shouldSetBookedToFalseForPetAppointments() {
        PetOwner owner = new PetOwner("John", "Doe",
                "john@example.com", "password");

        userRepository.save(owner);
        Pet pet = new Pet("Buddy", "Dog", "Male",
                LocalDate.of(2020, 1, 1), owner);

        petRepository.save(pet);
        AvailableSlot slot = new AvailableSlot(LocalDateTime.now().plusDays(1), true, null);
        slotRepository.save(slot);
        Appointment appointment = new Appointment("Checkup", pet, null, slot);
        appointmentRepository.save(appointment);
        AvailableSlot savedSlot = slotRepository.findById(slot.getId()).orElseThrow();
        assertTrue(savedSlot.isBooked());
        slotRepository.resetSlotsForPet(pet.getId());
        entityManager.clear();
        AvailableSlot updatedSlot = slotRepository.findById(slot.getId()).orElseThrow();
        assertFalse(updatedSlot.isBooked());
    }
}

