package com.vet_care.demo.service;

import com.vet_care.demo.dto.PetsPageProjection;
import com.vet_care.demo.model.*;
import com.vet_care.demo.repository.AvailableSlotRepository;
import com.vet_care.demo.repository.DoctorRepository;
import com.vet_care.demo.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alish
 */
@Service
public class PetService {
    private final PetCacheService petCacheService;
    private final PetRepository petRepository;
    private final AvailableSlotRepository availableSlotRepository;
    private final DoctorRepository doctorRepository;
    private final CacheManager cacheManager;

    public PetService(PetCacheService petCacheService,
                      DoctorRepository doctorRepository,
                      AvailableSlotRepository availableSlotRepository,
                      PetRepository petRepository,
                      CacheManager cacheManager) {
        this.petCacheService = petCacheService;
        this.doctorRepository = doctorRepository;
        this.availableSlotRepository = availableSlotRepository;
        this.petRepository = petRepository;
        this.cacheManager = cacheManager;
    }

    public List<PetsPageProjection> getPetsDtosByOwner(PetOwner owner) {
        return petRepository.findByOwner(owner);
    }

    public Map<String, Object> getPetsModel(AbstractUser user) {
        Map<String, Object> modelData = new HashMap<>();
        if(user.getRole() == Role.ROLE_DOCTOR) {
            modelData.put("pets", petCacheService.getPetsByDoctor((Doctor)user));
        } else {
            modelData.put("pets", petCacheService.getPetsByOwner((PetOwner) user));
            modelData.put("speciesList", Species.values());
            modelData.put("genderList", Gender.values());
        }
        return modelData;
    }

    @CacheEvict(value = "petsByOwner", key = "#owner.id")
    public Pet createPet(PetOwner owner, Pet pet) {
        if (pet.getId() != null) {
            throw new IllegalArgumentException(
                    "Cannot create pet with existing ID. Use updatePet for existing pets."
            );
        }
        pet.setOwner(owner);
        return petRepository.save(pet);
    }

    @CacheEvict(value = "petsByOwner", key = "#owner.id")
    public Pet updatePet(Long id, Pet petData, PetOwner owner) {
        evictPetCacheForDoctors(id);
        Pet updatedPet = petRepository.findById(id)
                .map(existingPet -> {
                    existingPet.setName(petData.getName());
                    existingPet.setSpecies(petData.getSpecies());
                    existingPet.setGender(petData.getGender());
                    existingPet.setBirthDate(petData.getBirthDate());
                    return petRepository.save(existingPet);
                })
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

        return updatedPet;
    }

    @CacheEvict(value = "petsByOwner", key = "#owner.id")
    public void deletePet(Long id, PetOwner owner) {
        evictPetCacheForDoctors(id);
        availableSlotRepository.resetSlotsForPet(id);
        petRepository.deleteById(id);
    }

    private void evictPetCacheForDoctors(Long petId) {
        List<Long> doctorIds = doctorRepository.findDoctorIdsByPet(petId);
        Cache cache = cacheManager.getCache("petsByDoctor");
        if (cache != null) {
            doctorIds.forEach(cache::evict);
        }
    }
}

