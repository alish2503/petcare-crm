package com.vet_care.demo.controller;

import com.vet_care.demo.dto.MedicalRecordsPageProjection;
import com.vet_care.demo.model.Doctor;
import com.vet_care.demo.model.MedicalRecord;
import com.vet_care.demo.security.CustomUserDetails;
import com.vet_care.demo.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pets/medical-records")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/{petId}")
    @PreAuthorize("@ownershipSecurityService.canAccessMedicalRecords(#petId, principal)")
    public String listMedicalRecordsForPet(@PathVariable Long petId, Model model) {
        String petName = medicalRecordService.getPetName(petId);
        List<MedicalRecordsPageProjection> projections = medicalRecordService.getMedicalRecordsByPetId(petId);
        model.addAttribute("petName", petName);
        model.addAttribute("petId", petId);
        model.addAttribute("medicalRecordsPageProjections", projections);
        return "medical-records";
    }

    @PostMapping("/{petId}")
    @PreAuthorize("hasRole('DOCTOR') and @ownershipSecurityService.isDoctorWithAppointment(#petId, principal)")
    public String createMedicalRecord(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @ModelAttribute @Valid MedicalRecord medicalRecord,
                                      @PathVariable Long petId) {

        Doctor doctor = (Doctor)userDetails.user();
        medicalRecordService.createMedicalRecord(doctor, medicalRecord, petId);
        return "redirect:/pets/medical-records/" + petId;
    }
}


