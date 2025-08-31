package com.vet_care.demo.controller;

import com.vet_care.demo.model.*;
import com.vet_care.demo.security.CustomUserDetails;
import com.vet_care.demo.service.PetService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Alish
 */

@Controller
@RequestMapping("/pets")
public class PetsController {

    private final PetService petService;

    public PetsController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public String listPets(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        AbstractUser user = userDetails.user();
        Map<String, Object> modelData = petService.getPetsModel(user);
        model.addAllAttributes(modelData);
        return "pets";
    }

    @PostMapping
    @PreAuthorize("hasRole('PET_OWNER')")
    public String createPet(@AuthenticationPrincipal CustomUserDetails userDetails,
                            @ModelAttribute @Valid Pet pet) {

        PetOwner owner = (PetOwner)userDetails.user();
        petService.createPet(owner, pet);
        return "redirect:/pets";
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PET_OWNER') and @ownershipSecurityService.isOwner(#id, principal)")
    public String updatePet(@AuthenticationPrincipal CustomUserDetails userDetails,
                            @PathVariable Long id, @ModelAttribute @Valid Pet pet) {

        PetOwner owner = (PetOwner)userDetails.user();
        petService.updatePet(id, pet, owner);
        return "redirect:/pets";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PET_OWNER') and @ownershipSecurityService.isOwner(#id, principal)")
    public String deletePet(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        PetOwner owner = (PetOwner)userDetails.user();
        petService.deletePet(id, owner);
        return "redirect:/pets";
    }
}

