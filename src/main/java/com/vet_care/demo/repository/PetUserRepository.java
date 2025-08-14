package com.vet_care.demo.repository;

import com.vet_care.demo.model.PetUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetUserRepository extends JpaRepository<PetUser, Long> {
    boolean existsByEmail(String email);
    @Query("SELECT u FROM PetUser u LEFT JOIN FETCH u.pets WHERE u.email = :email")
    Optional<PetUser> findByEmailWithPets(@Param("email") String email);
}
