package com.vet_care.demo.service;

import com.vet_care.demo.model.Doctor;
import com.vet_care.demo.model.Pet;
import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.repository.PetRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alish
 */
@Service
public class PetCacheService {
    private final PetRepository petRepository;

    public PetCacheService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Cacheable(value = "petsByOwner", key = "#owner.id")
    public List<Pet> getPetsByOwner(PetOwner owner) {
        return petRepository.findPetByOwner(owner);
    }

    @Cacheable(value = "petsByDoctor", key = "#doctor.id")
    public List<Pet> getPetsByDoctor(Doctor doctor) {
        return petRepository.findPetsByDoctor(doctor);
    }
}
