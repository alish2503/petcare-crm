package com.vet_care.demo.repository;

import com.vet_care.demo.dto.PetsPageProjection;
import com.vet_care.demo.model.Doctor;
import com.vet_care.demo.model.Pet;
import com.vet_care.demo.model.PetOwner;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("select p.name from Pet p where p.id = :id")
    String findNameById(@Param("id") Long id);
    List<Pet> findPetByOwner(PetOwner owner);

    @Query("SELECT DISTINCT p FROM Pet p JOIN Appointment a ON a.pet = p WHERE a.doctor = :doctor")
    List<Pet> findPetsByDoctor(@Param("doctor") Doctor doctor);
    List<PetsPageProjection> findByOwner(PetOwner owner);
}
