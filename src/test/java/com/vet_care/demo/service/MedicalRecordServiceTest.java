package com.vet_care.demo.service;

import com.vet_care.demo.model.Doctor;
import com.vet_care.demo.model.MedicalRecord;
import com.vet_care.demo.model.Pet;
import com.vet_care.demo.repository.MedicalRecordRepository;
import com.vet_care.demo.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    private Doctor doctor;
    private Pet pet;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);

        pet = new Pet();
        pet.setId(10L);
    }

    @Test
    void createMedicalRecord_ShouldSaveRecord_WhenPetExists() {
        MedicalRecord record = new MedicalRecord();
        record.setId(null);

        when(petRepository.findById(10L)).thenReturn(java.util.Optional.of(pet));
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        MedicalRecord result = medicalRecordService.createMedicalRecord(doctor, record, 10L);

        assertNotNull(result);
        assertEquals(pet, result.getPet());
        assertEquals(doctor, result.getDoctor());
        verify(medicalRecordRepository).save(record);
    }

    @Test
    void createMedicalRecord_ShouldThrow_WhenRecordHasId() {
        MedicalRecord record = new MedicalRecord();
        record.setId(5L);

        assertThrows(IllegalArgumentException.class,
                () -> medicalRecordService.createMedicalRecord(doctor, record, 10L));
    }

    @Test
    void createMedicalRecord_ShouldThrow_WhenPetNotFound() {
        MedicalRecord record = new MedicalRecord();
        record.setId(null);

        when(petRepository.findById(10L)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> medicalRecordService.createMedicalRecord(doctor, record, 10L));
        verify(medicalRecordRepository, never()).save(any());
    }
}

