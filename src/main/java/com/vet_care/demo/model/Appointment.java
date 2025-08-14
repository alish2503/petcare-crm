package com.vet_care.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
public class Appointment {
    @Id
    @GeneratedValue
    private Long id;

    @Future(message = "Appointment must be in the future")
    private LocalDateTime dateTime;

    @NotBlank(message = "Reason is required")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "slot_id")
    private AvailableSlot slot;

    public AvailableSlot getSlot() {
        return slot;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setSlot(AvailableSlot slot) {
        this.slot = slot;
    }
}
