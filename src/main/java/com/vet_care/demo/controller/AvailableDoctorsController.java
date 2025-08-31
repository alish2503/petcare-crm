package com.vet_care.demo.controller;

import com.vet_care.demo.dto.DoctorDto;
import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.security.CustomUserDetails;
import com.vet_care.demo.service.AvailableDoctorsService;
import com.vet_care.demo.service.PetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

/**
 * @author Alish
 */

@Controller
public class AvailableDoctorsController {
    private final AvailableDoctorsService availableDoctorsService;
    private final PetService petService;

    public AvailableDoctorsController(AvailableDoctorsService availableDoctorsService,
                                      PetService petService) {

        this.availableDoctorsService = availableDoctorsService;
        this.petService = petService;
    }

    @GetMapping("/appointments/available-doctors")
    @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public String showAvailableDoctors(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        PetOwner owner = (PetOwner)userDetails.user();
        List<DoctorDto> doctorDtos = availableDoctorsService.getDoctorsWithAvailableFutureSlots();
        model.addAttribute("doctorDtos", doctorDtos);
        model.addAttribute("petsPageProjections", petService.getPetsDtosByOwner(owner));
        return "appointment-booking";
    }
}
