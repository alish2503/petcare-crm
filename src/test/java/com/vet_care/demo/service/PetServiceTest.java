package com.vet_care.demo.service;

import com.vet_care.demo.model.*;
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

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private AvailableSlotRepository availableSlotRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Mock
    private PetCacheService petCacheService;

    @InjectMocks
    private PetService petService;

    private PetOwner owner;
    private Pet pet;

    @BeforeEach
    void initData() {
        owner = new PetOwner("John", "Doe", "john@mail.com", "password");
        owner.setRole(Role.ROLE_PET_OWNER);
        pet = new Pet("Rex", Species.DOG, Gender.MALE,
                LocalDate.of(2020, 1, 1), owner);
        pet.setId(1L);
    }

    @Test
    void getPetsModel_ForDoctor_ShouldReturnPets() {
        Doctor doctor = new Doctor();
        doctor.setRole(Role.ROLE_DOCTOR);
        List<Pet> pets = List.of(pet);
        when(petCacheService.getPetsByDoctor(any(Doctor.class))).thenReturn(pets);
        Map<String, Object> model = petService.getPetsModel(doctor);
        assertTrue(model.containsKey("pets"));
        assertEquals(pets, model.get("pets"));
    }

    @Test
    void getPetsModel_ForOwner_ShouldReturnPetsAndEnums() {
        List<Pet> pets = List.of(pet);
        when(petCacheService.getPetsByOwner(any(PetOwner.class))).thenReturn(pets);
        Map<String, Object> model = petService.getPetsModel(owner);
        assertTrue(model.containsKey("pets"));
        assertTrue(model.containsKey("speciesList"));
        assertTrue(model.containsKey("genderList"));
        assertEquals(pets, model.get("pets"));
    }

    @Test
    void createPet_ShouldSavePet() {
        Pet newPet = new Pet("Kitty", Species.CAT, Gender.FEMALE, LocalDate.now(), owner);
        when(petRepository.save(newPet)).thenReturn(newPet);
        Pet result = petService.createPet(owner, newPet);
        assertEquals(newPet, result);
        verify(petRepository).save(newPet);
    }

    @Test
    void createPet_WithId_ShouldThrowException() {
        Pet invalidPet = new Pet();
        invalidPet.setId(99L);
        assertThrows(IllegalArgumentException.class, () -> petService.createPet(owner, invalidPet));
    }

    @Test
    void updatePet_ShouldUpdateAndEvictDoctorCache() {
        Pet updated = new Pet("NewName", Species.CAT, Gender.FEMALE, LocalDate.now(), owner);
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenAnswer(inv -> inv.getArgument(0));
        when(doctorRepository.findDoctorIdsByPet(1L)).thenReturn(List.of(10L));
        when(cacheManager.getCache("petsByDoctor")).thenReturn(cache);
        Pet result = petService.updatePet(1L, updated, owner);
        assertEquals("NewName", result.getName());
        verify(cache).evict(10L);
    }

    @Test
    void updatePet_NotFound_ShouldThrow() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> petService.updatePet(1L, pet, owner));
    }

    @Test
    void deletePet_ShouldDeleteAndEvictDoctorCache() {
        when(doctorRepository.findDoctorIdsByPet(1L)).thenReturn(List.of(20L));
        when(cacheManager.getCache("petsByDoctor")).thenReturn(cache);
        petService.deletePet(1L, owner);
        verify(availableSlotRepository).resetSlotsForPet(1L);
        verify(petRepository).deleteById(1L);
        verify(cache).evict(20L);
    }
}
