package com.vet_care.demo.controller;

import com.vet_care.demo.dto.MedicalRecordsPageProjection;
import com.vet_care.demo.model.MedicalRecord;
import com.vet_care.demo.model.Pet;
import com.vet_care.demo.model.PetUser;
import com.vet_care.demo.repository.MedicalRecordRepository;
import com.vet_care.demo.repository.PetRepository;
import com.vet_care.demo.service.MedicalRecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/medical-records/{petId}")
    @PreAuthorize("@ownershipSecurityService.isPetOwner(#petId, principal)")
    public String listMedicalRecordsForPet(@PathVariable Long petId, Model model) {
        String petName = medicalRecordService.getPetName(petId);
        List<MedicalRecordsPageProjection> projections = medicalRecordService.getMedicalRecordsByPetId(petId);

        model.addAttribute("petName", petName);
        model.addAttribute("medicalRecordsPageProjections", projections);

        return "medical-records";
    }
}


