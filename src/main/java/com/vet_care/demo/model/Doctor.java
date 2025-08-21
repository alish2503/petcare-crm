package com.vet_care.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Doctor extends AbstractUser {

    String specialization;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    private List<AvailableSlot> availableSlots = new ArrayList<>();

    public Doctor(String firstName, String lastName, String email, String password, String specialization) {
        super(firstName, lastName, email, password);
        this.specialization = specialization;
    }

    public Doctor() {
        super();
    }

    public String getSpecialization() {
        return specialization;
    }

    public List<AvailableSlot> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<AvailableSlot> availableSlots) {
        this.availableSlots = availableSlots;
    }

    public void setSpecialization(String surgeon) {
    }
}
