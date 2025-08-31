package com.vet_care.demo.repository;

import com.vet_care.demo.model.Appointment;
import com.vet_care.demo.model.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alish
 */
@DataJpaTest
public class AvailableSlotRepositoryTest {

    @Autowired
    private AvailableSlotRepository slotRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void resetSlotsForPet_shouldSetBookedToFalseForExistingPetAppointments() {
        Pet pet = petRepository.findById(1L).orElseThrow();

        List<Appointment> appointments = appointmentRepository.findAllByPet(pet);
        assertFalse(appointments.isEmpty(), "Pet should have appointments");

        for (Appointment a : appointments) {
            assertTrue(a.getSlot().isBooked(), "Slot should initially be booked");
        }

        slotRepository.resetSlotsForPet(pet.getId());
        entityManager.clear();

        appointments = appointmentRepository.findAllByPet(pet);
        for (Appointment a : appointments) {
            assertFalse(a.getSlot().isBooked());
        }
    }
}


