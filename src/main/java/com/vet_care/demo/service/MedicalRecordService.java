package com.vet_care.demo.service;

import com.vet_care.demo.dto.MedicalRecordsPageProjection;
import com.vet_care.demo.model.Doctor;
import com.vet_care.demo.model.MedicalRecord;
import com.vet_care.demo.model.Pet;
import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.repository.MedicalRecordRepository;
import com.vet_care.demo.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alish
 */
@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PetRepository petRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PetRepository petRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.petRepository = petRepository;
    }

    public List<MedicalRecordsPageProjection> getMedicalRecordsByPetId(Long petId) {
        return medicalRecordRepository.findByPetIdOrderByRecordDateDesc(petId);
    }

    public MedicalRecord createMedicalRecord(Doctor doctor, MedicalRecord medicalRecord, Long petId) {
        if (medicalRecord.getId() != null) {
            throw new IllegalArgumentException(
                    "Cannot create medical record with existing ID."
            );
        }
        Pet pet = petRepository.findById(petId).orElseThrow(
                () -> new EntityNotFoundException("Pet not found")
        );
        medicalRecord.setPet(pet);
        medicalRecord.setDoctor(doctor);
        return medicalRecordRepository.save(medicalRecord);
    }

    public String getPetName(Long petId) {
        return petRepository.findNameById(petId);
    }
}

