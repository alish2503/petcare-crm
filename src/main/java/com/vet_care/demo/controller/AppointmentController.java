package com.vet_care.demo.controller;

import com.vet_care.demo.model.*;
import com.vet_care.demo.repository.AppointmentRepository;
import com.vet_care.demo.repository.AvailableSlotRepository;
import com.vet_care.demo.repository.PetRepository;
import com.vet_care.demo.repository.VeterinarianRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentController {
    private final AppointmentRepository appointmentRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final PetRepository petRepository;
    private final AvailableSlotRepository availableSlotRepository;

    public AppointmentController(AppointmentRepository appointmentRepository,
                                 VeterinarianRepository vetRepository, PetRepository petRepository,
                                 AvailableSlotRepository availableSlotRepository) {

        this.appointmentRepository = appointmentRepository;
        this.veterinarianRepository = vetRepository;
        this.petRepository = petRepository;
        this.availableSlotRepository = availableSlotRepository;
    }

    @GetMapping("/appointments")
    public String showAppointmentForm(Model model, HttpSession session) {
        PetOwner user = (PetOwner) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<Veterinarian> veterinarians = veterinarianRepository.findAllWithFutureSlots();

        LocalDateTime now = LocalDateTime.now();
        for (Veterinarian veterinarian : veterinarians) {
            List<AvailableSlot> filteredSlots = veterinarian.getAvailableSlots().stream()
                    .filter(slot -> slot.getDateTime().isAfter(now) && !slot.isBooked())
                    .collect(Collectors.toList());
            veterinarian.setAvailableSlots(filteredSlots);
        }

        model.addAttribute("veterinarians", veterinarians);
        model.addAttribute("pets", user.getPets());
        return "appointment-booking";
    }

    @Transactional
    @PostMapping("/appointments/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();

        AvailableSlot slot = appointment.getSlot();
        slot.setBooked(false);
        availableSlotRepository.save(slot);

        appointmentRepository.delete(appointment);
        return "redirect:/appointments";
    }

    @Transactional
    @PostMapping("/appointment-booking/{slotId}")
    public String addAppointment(@ModelAttribute @Valid Appointment appointment,
                                    @RequestParam Long petId,
                                    @RequestParam Long vetId,
                                    @PathVariable Long slotId,
                                    HttpSession session) {

        PetOwner user = (PetOwner) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        Pet pet = petRepository.findById(petId).orElseThrow();
        if (!pet.getOwner().getId().equals(user.getId())) {
            return "redirect:/appointments";
        }

        Veterinarian veterinarian = veterinarianRepository.findById(vetId).orElseThrow();

        AvailableSlot slot = availableSlotRepository.findById(slotId).orElseThrow();
        LocalDateTime slotDateTime = slot.getDateTime();

        boolean alreadyExists = appointmentRepository.existsByDateTimeAndVeterinarian(slotDateTime, veterinarian);
        if (alreadyExists) {
            return "redirect:/appointments";
        }

        slot.setBooked(true);
        availableSlotRepository.save(slot);

        appointment.setDateTime(slotDateTime);
        appointment.setPet(pet);
        appointment.setVeterinarian(veterinarian);

        appointmentRepository.save(appointment);

        return "redirect:/appointments";
    }


}
