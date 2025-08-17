package com.vet_care.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author Alish
 */
@Entity
public class AvailableSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Future(message = "Slot for appointment must be in the future")
    private LocalDateTime dateTime;
    private boolean booked = false;

    @ManyToOne
    private Doctor doctor;

    public AvailableSlot() {}
    public AvailableSlot(LocalDateTime dateTime, boolean booked, Doctor doctor) {
        this.dateTime = dateTime;
        this.booked = booked;
        this.doctor = doctor;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
