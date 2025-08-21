package com.vet_care.demo.repository;

import com.vet_care.demo.model.Pet;
import com.vet_care.demo.model.PetOwner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alish
 */
@DataJpaTest
public class PetOwnerRepositoryTest {

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Test
    void findByEmailWithPets_ShouldReturnUserWithPets_WhenUserExists() {
        Optional<PetOwner> optionalUser = petOwnerRepository.findByEmailWithPets("john@example.com");
        assertTrue(optionalUser.isPresent());
        PetOwner user = optionalUser.get();
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(1, user.getPets().size());
        Pet pet = user.getPets().get(0);
        assertEquals("Buddy", pet.getName());
        assertEquals(user.getId(), pet.getOwner().getId());
    }

    @Test
    void findByEmailWithPets_ShouldReturnEmpty_WhenUserDoesNotExist() {
        Optional<PetOwner> optionalUser = petOwnerRepository.findByEmailWithPets("nonexistent@example.com");
        assertFalse(optionalUser.isPresent());
    }
}
