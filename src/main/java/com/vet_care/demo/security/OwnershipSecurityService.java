package com.vet_care.demo.security;

import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.service.PetService;
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

    public boolean isOwner(Long petId, CustomUserDetails userDetails) {
        PetOwner owner = userDetails.getUser();
        return petService.getPetById(petId).getOwner().getId().equals(owner.getId());
    }
}
