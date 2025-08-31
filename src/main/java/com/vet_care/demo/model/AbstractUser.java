package com.vet_care.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * @author Alish
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractUser extends BaseEntity {

    @Column(nullable = false)
    @Size(min = 2, message = "First name must be at least 2 characters")
    @Pattern(regexp = "^[A-Za-z\\s\\-]+$", message = "Name must contain only letters, spaces, or hyphens")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(nullable = false)
    @Size(min = 2, message = "First name must be at least 2 characters")
    @Pattern(regexp = "^[A-Za-z\\s\\-]+$", message = "Name must contain only letters, spaces, or hyphens")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is required")
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public AbstractUser() {}

    public AbstractUser(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
