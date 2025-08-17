package com.vet_care.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @Pattern(regexp = "^(Dog|Cat|Bird)$", message = "Species must be dog, cat or bird")
    private String species;

    @Pattern(regexp = "^(Male|Female)$", message = "Gender must be male or female")
    private String gender;

    @NotNull(message = "Birth date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Birth date cannot be in the future")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private PetUser owner;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    public Pet() {}

    public Pet(String name, String species, String gender, LocalDate birthDate, PetUser owner) {
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

    public String getSpecies() {
        return species;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public Long getId() { return id; }

    public PetUser getOwner() { return owner; }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setOwner(PetUser owner) { this.owner = owner; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
