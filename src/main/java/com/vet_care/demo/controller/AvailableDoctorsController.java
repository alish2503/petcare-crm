package com.vet_care.demo.controller;

import com.vet_care.demo.dto.DoctorDTO;
import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.security.CustomUserDetails;
import com.vet_care.demo.service.AvailableDoctorsService;
import com.vet_care.demo.service.PetService;
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

    @GetMapping("/availableDoctors")
    public String showAvailableDoctors(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        PetOwner owner = userDetails.getUser();
        List<DoctorDTO> doctorDTOs = availableDoctorsService.getDoctorsWithAvailableFutureSlots();

        model.addAttribute("doctorDTOs", doctorDTOs);
        model.addAttribute("pets", petService.getPetsByOwner(owner));

        return "appointment-booking";
    }
}
