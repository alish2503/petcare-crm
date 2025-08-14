package com.vet_care.demo.dto;

import java.time.LocalDate;

/**
 * @author Alish
 */
public interface MedicalRecordsPageProjection {
    Long getId();
    DoctorInfo getDoctor();
    String getTreatment();
    String getDiagnosis();
    LocalDate getDate();

    interface DoctorInfo {
        String getLastName();
    }
}
