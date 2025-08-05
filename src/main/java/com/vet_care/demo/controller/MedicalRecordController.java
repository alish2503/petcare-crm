package com.vet_care.demo.controller;

import com.vet_care.demo.model.MedicalRecord;
import com.vet_care.demo.model.Pet;
import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.repository.MedicalRecordRepository;
import com.vet_care.demo.repository.PetRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MedicalRecordController {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PetRepository petRepository;

    public MedicalRecordController(MedicalRecordRepository medicalRecordRepository,
                                   PetRepository petRepository) {

        this.medicalRecordRepository = medicalRecordRepository;
        this.petRepository = petRepository;
    }

    @GetMapping("/medical-records/selected")
    public String showMedicalRecords(@RequestParam(required = false) Long petId,
                                     HttpSession session, Model model) {

        PetOwner user = (PetOwner) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        List<MedicalRecord> records;
        Pet pet = null;
        if (petId != null) {
            pet = petRepository.findById(petId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid pet ID"));

            if (!pet.getOwner().getId().equals(user.getId())) {
                return "redirect:/medical-records";
            }
            records = medicalRecordRepository.findAllByPetIdWithVet(petId);
        } else {
            records = medicalRecordRepository.findAllByUser(user);
        }

        model.addAttribute("petName", pet != null ? pet.getName() : null);
        model.addAttribute("medicalRecords", records);

        return "pet-records";
    }


    @GetMapping("/medical-records")
    public String showPetSelection(Model model, HttpSession session) {

        PetOwner user = (PetOwner) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("pets", user.getPets());

        return "medical-records";
    }


}

