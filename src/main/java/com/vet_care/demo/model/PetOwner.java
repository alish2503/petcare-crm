package com.vet_care.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PetOwner extends AbstractUser {

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    public PetOwner() {
        super();
    }
    public PetOwner(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    public List<Pet> getPets() {
        return pets;
    }
}
