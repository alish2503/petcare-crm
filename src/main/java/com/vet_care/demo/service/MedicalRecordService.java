package com.vet_care.demo.service;

import com.vet_care.demo.dto.MedicalRecordsPageProjection;
import com.vet_care.demo.model.MedicalRecord;
import com.vet_care.demo.model.Pet;
import com.vet_care.demo.repository.MedicalRecordRepository;
import com.vet_care.demo.repository.PetRepository;
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
        return medicalRecordRepository.findByPetId(petId);
    }

    public String getPetName(Long petId) {
        return petRepository.findNameById(petId);
    }
}

