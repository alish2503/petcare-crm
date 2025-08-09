package com.vet_care.demo.repository;

import com.vet_care.demo.model.MedicalRecord;
import com.vet_care.demo.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Query("""
        SELECT r FROM MedicalRecord r
        JOIN FETCH r.pet
        JOIN FETCH r.veterinarian
        WHERE r.pet.owner = :owner
    """)
    List<MedicalRecord> findAllByUser(@Param("owner") PetOwner owner);

    @Query("SELECT r FROM MedicalRecord r JOIN FETCH r.veterinarian WHERE r.pet.id = :petId")
    List<MedicalRecord> findAllByPetIdWithVet(@Param("petId") Long petId);
}
