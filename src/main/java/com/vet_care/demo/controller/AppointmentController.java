package com.vet_care.demo.controller;

import com.vet_care.demo.model.*;
import com.vet_care.demo.security.CustomUserDetails;
import com.vet_care.demo.service.AppointmentService;
import jakarta.validation.Valid;
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
        PetUser owner = userDetails.getUser();
        model.addAttribute("appointmentsPageProjections", appointmentService.getAppointmentsForUser(owner));
        return "appointments";
    }

    @PostMapping
    @PreAuthorize("@ownershipSecurityService.isPetOwner(#petId, principal)")
    public String addAppointment(@ModelAttribute @Valid Appointment appointment,
                                 @RequestParam Long petId,
                                 @RequestParam Long doctorId,
                                 @RequestParam Long slotId) {

        appointmentService.addAppointment(appointment, petId, doctorId, slotId);
        return "redirect:/appointments";
    }

    @DeleteMapping("/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }
}

