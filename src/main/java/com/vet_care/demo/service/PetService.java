package com.vet_care.demo.service;

import com.vet_care.demo.model.AvailableSlot;
import com.vet_care.demo.model.Pet;
import com.vet_care.demo.model.PetUser;
import com.vet_care.demo.repository.AppointmentRepository;
import com.vet_care.demo.repository.AvailableSlotRepository;
import com.vet_care.demo.repository.PetRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alish
 */
@Service
public class PetService {

    private final PetRepository petRepository;
    private final AvailableSlotRepository availableSlotRepository;

    public PetService(PetRepository petRepository, AvailableSlotRepository availableSlotRepository) {
        this.petRepository = petRepository;
        this.availableSlotRepository = availableSlotRepository;
    }

    @Cacheable("pets")
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
    }

    public List<Pet> getPetsByOwner(PetUser owner) {
        return owner.getPets();
    }

    public Pet createPet(PetUser owner, Pet pet) {
        pet.setOwner(owner);
        Pet createdPet = petRepository.save(pet);
        owner.getPets().add(createdPet);
        return createdPet;
    }

    public Pet updatePet(Long id, Pet petData, PetUser owner) {
        Pet updatedPet = petRepository.findById(id)
                .map(existingPet -> {
                    existingPet.setName(petData.getName());
                    existingPet.setSpecies(petData.getSpecies());
                    existingPet.setGender(petData.getGender());
                    existingPet.setBirthDate(petData.getBirthDate());
                    return petRepository.save(existingPet);
                })
                .orElseThrow(() -> new IllegalArgumentException("Pet not found with id " + id));

        owner.getPets().replaceAll(existing ->
                existing.getId().equals(id) ? updatedPet : existing
        );
        return updatedPet;
    }

    public void deletePet(Long id, PetUser owner) {
        owner.getPets().removeIf(p -> p.getId().equals(id));
        availableSlotRepository.resetSlotsForPet(id);
        petRepository.deleteById(id);
    }
}

