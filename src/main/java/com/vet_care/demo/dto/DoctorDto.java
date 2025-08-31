package com.vet_care.demo.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
public class DoctorDto {
    private Long id;
    private String lastName;
    private String specialization;

    public List<SlotDTO> getAvailableSlots() {
        return availableSlots;
    }

    private final List<SlotDTO> availableSlots = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public static class SlotDTO {
        private Long id;
        private LocalDateTime slotTime;
        private boolean booked;

        public SlotDTO(Long id, LocalDateTime slotTime, boolean booked) {
            this.id = id;
            this.slotTime = slotTime;
            this.booked = booked;
        }

        public Long getId() {
            return id;
        }

        public LocalDateTime getSlotTime() {
            return slotTime;
        }

        public boolean isBooked() {
            return booked;
        }
    }
}
