package com.vet_care.demo.service;

import com.vet_care.demo.model.AvailableSlot;
import com.vet_care.demo.model.Doctor;
import com.vet_care.demo.repository.AvailableSlotRepository;
import com.vet_care.demo.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Alish
 */
@Service
public class SlotService {
    private final AvailableSlotRepository availableSlotRepository;

    public SlotService(AvailableSlotRepository availableSlotRepository) {
        this.availableSlotRepository = availableSlotRepository;
    }

    private Doctor getCurrentDoctor() {
        CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return (Doctor)userDetails.user();
    }

    public List<AvailableSlot> getSlotsForCurrentDoctor() {
        return availableSlotRepository.findByDoctorOrderBySlotTimeAsc(getCurrentDoctor());
    }

    public List<AvailableSlot> addSlotsForCurrentDoctor(List<String> dateTimes) {
        Doctor doctor = getCurrentDoctor();
        List<AvailableSlot> slots = dateTimes.stream()
                .map(dt -> LocalDateTime.parse(dt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .filter(dt -> !availableSlotRepository.existsByDoctorAndSlotTime(doctor, dt))
                .map(dt -> new AvailableSlot(dt, doctor))
                .toList();
        return availableSlotRepository.saveAll(slots);
    }

    public void deleteSlotForCurrentDoctor(Long slotId) {
        AvailableSlot slot = availableSlotRepository.findById(slotId)
                .orElseThrow(() -> new EntityNotFoundException("Slot not found"));

        if (slot.isBooked()) {
            throw new IllegalStateException("You are not allowed to delete this slot. It is already booked");
        }
        if (!slot.getDoctor().getId().equals(getCurrentDoctor().getId())) {
            throw new AccessDeniedException("You can only delete your own slots");
        }
        availableSlotRepository.delete(slot);
    }
}
