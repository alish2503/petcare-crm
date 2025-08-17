package com.vet_care.demo.dto;

import java.time.LocalDateTime;

/**
 * @author Alish
 */
public interface FlatDoctorSlot {
    Long getDoctorId();
    String getLastName();
    String getSpecialization();
    Long getSlotId();
    LocalDateTime getDateTime();
    boolean isBooked();
}
