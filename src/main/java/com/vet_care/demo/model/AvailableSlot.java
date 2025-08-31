package com.vet_care.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author Alish
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"doctor_id", "slotTime"})
})
public class AvailableSlot extends BaseEntity {

    @Column(nullable = false)
    @NotNull(message = "Slot date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime slotTime;

    @Column(nullable = false)
    private boolean booked = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    public AvailableSlot() {}
    public AvailableSlot(LocalDateTime dateTime, Doctor doctor) {
        this.slotTime = dateTime;
        this.doctor = doctor;
    }

    public LocalDateTime getSlotTime() {
        return slotTime;
    }

    public boolean isBooked() {
        return booked;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public void setSlotTime(LocalDateTime slotTime) {
        this.slotTime = slotTime;
    }
}
