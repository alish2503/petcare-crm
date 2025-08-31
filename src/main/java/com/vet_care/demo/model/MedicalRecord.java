package com.vet_care.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class MedicalRecord extends BaseEntity {

    @Column(nullable = false)
    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    @Column(nullable = false)
    @NotBlank(message = "Treatment is required")
    private String treatment;

    @Column(nullable = false)
    @NotNull(message = "Record date is required")
    private LocalDate recordDate = LocalDate.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    public MedicalRecord() {}
    public MedicalRecord(String diagnosis, String treatment, LocalDate date, Pet pet, Doctor doctor) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.recordDate = date;
        this.pet = pet;
        this.doctor = doctor;
    }

    public Pet getPet() {
        return pet;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
}
