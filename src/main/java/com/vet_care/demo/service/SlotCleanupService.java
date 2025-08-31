package com.vet_care.demo.service;

import com.vet_care.demo.model.AvailableSlot;
import com.vet_care.demo.repository.AvailableSlotRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */
@Service
public class SlotCleanupService {
    private final AvailableSlotRepository slotRepository;

    public SlotCleanupService(AvailableSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanOldSlots() {
        LocalDateTime now = LocalDateTime.now();
        List<AvailableSlot> oldSlots = slotRepository.findAllBySlotTimeBeforeAndBookedFalse(now);
        slotRepository.deleteAll(oldSlots);
    }
}
