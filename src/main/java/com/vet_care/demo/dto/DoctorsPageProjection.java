package com.vet_care.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */
public interface DoctorsPageProjection {
    Long getId();
    String getLastName();
    String getSpecialization();
    List<AvailableSlotSummary> getAvailableSlots();

    interface AvailableSlotSummary {
        Long getId();
        LocalDateTime getDateTime();
        boolean isBooked();
    }
}
