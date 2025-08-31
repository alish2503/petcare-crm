package com.vet_care.demo.repository;

import com.vet_care.demo.dto.MedicalRecordsPageProjection;
import com.vet_care.demo.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecordsPageProjection> findByPetIdOrderByRecordDateDesc(Long petId);
}
