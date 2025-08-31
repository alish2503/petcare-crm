package com.vet_care.demo.controller;

/**
 * @author Alish
 */

import com.vet_care.demo.service.SlotService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/doctor-slots")
@PreAuthorize("hasRole('DOCTOR')")
public class SlotsController {
    private final SlotService slotService;

    public SlotsController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping
    String listSlots(Model model) {
        model.addAttribute("savedSlots", slotService.getSlotsForCurrentDoctor());
        return "doctor-slots";
    }

    @PostMapping
    String addSlots(@RequestParam("slots") List<String> dateTimes) {
        slotService.addSlotsForCurrentDoctor(dateTimes);
        return "redirect:/doctor-slots";
    }

    @DeleteMapping("/{id}")
    String deleteSlot(@PathVariable Long id) {
        slotService.deleteSlotForCurrentDoctor(id);
        return "redirect:/doctor-slots";
    }
}

