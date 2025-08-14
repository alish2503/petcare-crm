package com.vet_care.demo.controller;

import com.vet_care.demo.dto.DoctorsPageProjection;
import com.vet_care.demo.model.PetUser;
import com.vet_care.demo.security.CustomUserDetails;
import com.vet_care.demo.service.AvailableSlotService;
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
public class AvailableSlotsController {

    private final AvailableSlotService availableSlotService;
    private final PetService petService;

    public AvailableSlotsController(AvailableSlotService availableSlotService,
                                    PetService petService) {

        this.availableSlotService = availableSlotService;
        this.petService = petService;
    }

    @GetMapping("/availableSlots")
    public String showAvailableSlots(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        PetUser owner = userDetails.getUser();
        List<DoctorsPageProjection> doctorsPageProjection = availableSlotService.getDoctorsWithAvailableFutureSlots();

        model.addAttribute("doctorsPageProjection", doctorsPageProjection);
        model.addAttribute("pets", petService.getPetsByOwner(owner));

        return "appointment-booking";
    }
}
