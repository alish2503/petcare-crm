package com.vet_care.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String diagnosis;
    private String treatment;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
