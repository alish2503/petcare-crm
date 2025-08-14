package com.vet_care.demo.dto;

import java.time.LocalDateTime;

/**
 * @author Alish
 */
public interface AppointmentsPageProjection {
    Long getId();
    String getPetName();
    String getDoctorLastName();
    LocalDateTime getDateTime();
    String getReason();
}
