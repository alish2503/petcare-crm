package com.vet_care.demo.repository;

import com.vet_care.demo.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<PetOwner, Long> {
    boolean existsByEmail(String email);
    Optional<PetOwner> findByEmail(String email);
}
