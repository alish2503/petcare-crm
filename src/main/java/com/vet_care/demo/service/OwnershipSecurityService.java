package com.vet_care.demo.service;

import com.vet_care.demo.model.Pet;
import com.vet_care.demo.model.PetUser;
import com.vet_care.demo.repository.PetRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */

@Service
public class OwnershipSecurityService {

    private final PetService petService;

    public OwnershipSecurityService(PetService petService) {
        this.petService = petService;
    }

    public boolean isPetOwner(Long petId, PetUser user) {
        return petService.getPetById(petId).getOwner().getId().equals(user.getId());
    }
}
