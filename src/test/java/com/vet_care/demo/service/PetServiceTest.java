package com.vet_care.demo.service;

import com.vet_care.demo.model.Pet;
import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.repository.AvailableSlotRepository;
import com.vet_care.demo.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private AvailableSlotRepository availableSlotRepository;

    @InjectMocks
    private PetService petService;

    private PetOwner owner;
    private Pet pet;

    @BeforeEach
    void setUp() {
        owner = new PetOwner("Alice", "Smith", "alice@example.com", "password");
        owner.getPets().clear();

        pet = new Pet("Buddy", "Dog", "Male",
                LocalDate.of(2020, 1, 1), null);

        pet.setId(1L);
    }

    @Test
    void getPetById_ShouldReturnPet_WhenFound() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        Pet result = petService.getPetById(1L);

        assertEquals(pet, result);
        verify(petRepository).findById(1L);
    }

    @Test
    void getPetById_ShouldThrowException_WhenNotFound() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> petService.getPetById(1L));

        assertEquals("Pet not found", exception.getMessage());
        verify(petRepository).findById(1L);
    }

    @Test
    void createPet_ShouldSaveAndAddPetToOwner() {
        pet.setId(null);

        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        Pet newPet = petService.createPet(owner, pet);

        assertEquals(pet, newPet);
        assertTrue(owner.getPets().contains(pet));
        verify(petRepository).save(pet);
    }

    @Test
    void createPet_ShouldThrowException_WhenIdIsNotNull() {
        pet.setId(5L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> petService.createPet(owner, pet));

        assertEquals(
                "Cannot create pet with existing ID. Use updatePet for existing pets.",
                ex.getMessage()
        );

        verify(petRepository, never()).save(any());
    }


    @Test
    void updatePet_ShouldUpdateExistingPet() {
        Pet updatedData = new Pet("Max", "Cat", "Female",
                LocalDate.of(2021, 5, 5), null);

        owner.getPets().add(pet);

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenAnswer(i -> i.getArgument(0));

        Pet updatedPet = petService.updatePet(1L, updatedData, owner);

        assertEquals("Max", updatedPet.getName());
        assertEquals("Cat", updatedPet.getSpecies());
        assertEquals("Female", updatedPet.getGender());
        assertEquals(LocalDate.of(2021, 5, 5), updatedPet.getBirthDate());

        assertEquals(1, owner.getPets().size());
        assertEquals(updatedPet, owner.getPets().get(0));

        verify(petRepository).findById(1L);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void updatePet_ShouldThrowException_WhenPetNotFound() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> petService.updatePet(1L, pet, owner));

        assertEquals("Pet not found with id 1", ex.getMessage());
        verify(petRepository).findById(1L);
        verify(petRepository, never()).save(any());
    }

    @Test
    void deletePet_ShouldRemovePetAndResetSlots() {
        owner.getPets().add(pet);

        petService.deletePet(1L, owner);

        assertFalse(owner.getPets().contains(pet));
        verify(availableSlotRepository).resetSlotsForPet(1L);
        verify(petRepository).deleteById(1L);
    }
}
