package com.vet_care.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue
    private Long id;

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

    public Appointment() {}
    public Appointment(String reason, Pet pet, Doctor doctor, AvailableSlot slot) {
        this.reason = reason;
        this.pet = pet;
        this.doctor = doctor;
        this.slot = slot;
    }

    public String getReason() {
        return reason;
    }

    public Pet getPet() {
        return pet;
    }

    public AvailableSlot getSlot() {
        return slot;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setSlot(AvailableSlot slot) {
        this.slot = slot;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
