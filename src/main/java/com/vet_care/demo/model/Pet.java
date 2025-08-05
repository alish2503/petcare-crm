package com.vet_care.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, message = "Name must be at least 2 characters")
    @Pattern(regexp = "^[A-Za-z\\s\\-]+$", message = "Name must contain only letters, spaces, or hyphens")
    @NotBlank(message = "Name is required")
    private String name;
    private String species;
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private PetOwner owner;

    @OneToMany(mappedBy = "pet")
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "pet")
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public String getName() {
        return name;
    }
    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public Long getId() { return id; }

    public PetOwner getOwner() { return owner; }

    public void setOwner(PetOwner owner) { this.owner = owner; }

}
