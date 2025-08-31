package com.vet_care.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pet extends BaseEntity {

    @Column(nullable = false)
    @Size(min = 2, message = "Name must be at least 2 characters")
    @Pattern(regexp = "^[A-Za-z\\s\\-]+$", message = "Name must contain only letters, spaces, or hyphens")
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Species is required")
    @Enumerated(EnumType.STRING)
    private Species species;

    @Column(nullable = false)
    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @NotNull(message = "Birth date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Birth date cannot be in the future")
    private LocalDate birthDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private PetOwner owner;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    public Pet() {}

    public Pet(String name, Species species, Gender gender, LocalDate birthDate, PetOwner owner) {
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.birthDate = birthDate;
        this.owner = owner;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public String getName() {
        return name;
    }

    public Species getSpecies() {
        return species;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public PetOwner getOwner() { return owner; }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setOwner(PetOwner owner) { this.owner = owner; }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
