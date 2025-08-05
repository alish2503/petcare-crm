package com.vet_care.demo.controller;

import com.vet_care.demo.model.Pet;
import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.repository.PetRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Alish
 */

@Controller
public class PetsController {
    private final PetRepository petRepository;

    public PetsController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GetMapping("/pets")
    public String showPets(HttpSession session, Model model) {
        PetOwner user = (PetOwner)session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("pets", user.getPets());
        model.addAttribute("pet", new Pet());
        return "pets";
    }

    @PostMapping("/pets/save")
    public String savePet(@ModelAttribute("pet") @Valid Pet pet,
                          BindingResult result,
                          HttpSession session,
                          Model model) {

        PetOwner user = (PetOwner) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("pets", user.getPets());
            return "pets";
        }

        if (pet.getId() != null) {
            Optional<Pet> existingPetOpt = petRepository.findById(pet.getId());
            if (existingPetOpt.isEmpty() || !existingPetOpt.get().getOwner().getId().equals(user.getId())) {
                return "redirect:/pets";
            }
        } else {
            pet.setOwner(user);
        }

        petRepository.save(pet);

        return "redirect:/pets";
    }

    @PostMapping("/delete/{id}")
    public String deletePet(@PathVariable Long id, HttpSession session) {
        PetOwner user = (PetOwner) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        Optional<Pet> petOptional = petRepository.findById(id);
        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            if (pet.getOwner().getId().equals(user.getId())) {
                petRepository.delete(pet);
            }
        }

        return "redirect:/pets";
    }
}
