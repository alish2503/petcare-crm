package com.vet_care.demo.security;

import com.vet_care.demo.model.*;
import com.vet_care.demo.repository.AppointmentRepository;
import com.vet_care.demo.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alish
 */

@Service
public class OwnershipSecurityService {
    private final PetRepository petRepository;
    private final AppointmentRepository appointmentRepository;

    public OwnershipSecurityService(PetRepository petRepository, AppointmentRepository appointmentRepository) {
        this.petRepository = petRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public boolean isOwner(Long petId, CustomUserDetails userDetails) {
        PetOwner owner = (PetOwner) userDetails.user();
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

        if (!pet.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("You are not the owner of this pet");
        }
        return true;
    }

    public boolean isDoctorWithAppointment(Long petId, CustomUserDetails userDetails) {
        Doctor doctor = (Doctor) userDetails.user();
        boolean exists = appointmentRepository.existsByDoctorAndPetId(doctor, petId);
        if (!exists) {
            throw new AccessDeniedException(
                    "You do not have an appointment with this pet and cannot access its records"
            );
        }
        return true;
    }

    public boolean canAccessMedicalRecords(Long petId, CustomUserDetails userDetails) {
        Role role = userDetails.user().getRole();
        if (role == Role.ROLE_PET_OWNER) {
            return isOwner(petId, userDetails);
        }
        return isDoctorWithAppointment(petId, userDetails);
    }
}
