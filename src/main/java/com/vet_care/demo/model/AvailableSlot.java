package com.vet_care.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * @author Alish
 */
@Entity
public class AvailableSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;
    private boolean booked = false;

    @ManyToOne
    private Doctor doctor;

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
