package com.vet_care.demo.repository;

import com.vet_care.demo.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT owner FROM PetOwner owner LEFT JOIN FETCH owner.pets WHERE owner.email = :email")
    Optional<PetOwner> findByEmailWithPets(@Param("email") String email);
}
