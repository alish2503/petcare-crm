package com.vet_care.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PetUser extends AbstractUser {

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    public PetUser() {
        super();
    }
    public PetUser(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    public List<Pet> getPets() {
        return pets;
    }
}
