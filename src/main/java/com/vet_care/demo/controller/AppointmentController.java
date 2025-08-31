package com.vet_care.demo.controller;

import com.vet_care.demo.model.*;
import com.vet_care.demo.security.CustomUserDetails;
import com.vet_care.demo.service.AppointmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String listAppointments(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        AbstractUser user = userDetails.user();
        model.addAttribute("appointmentsPageProjections", appointmentService.getAppointments(user));
        return "appointments";
    }

    @PostMapping
    @PreAuthorize("hasRole('PET_OWNER') and @ownershipSecurityService.isOwner(#petId, principal)")
    public String addAppointment(@RequestParam String reason,
                                 @RequestParam Long petId,
                                 @RequestParam Long doctorId,
                                 @RequestParam Long slotId) {

        appointmentService.addAppointment(reason, petId, doctorId, slotId);
        return "redirect:/appointments";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public String deleteAppointment(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        PetOwner user = (PetOwner)userDetails.user();
        appointmentService.deleteAppointment(id, user);
        return "redirect:/appointments";
    }
}

