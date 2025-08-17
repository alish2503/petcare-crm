package com.vet_care.demo.repository;

import com.vet_care.demo.dto.AppointmentsPageProjection;
import com.vet_care.demo.model.Appointment;
import com.vet_care.demo.model.Doctor;
import com.vet_care.demo.model.PetUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsBySlot_DateTimeAndDoctor(LocalDateTime dateTime, Doctor doctor);

    @Query("""
        SELECT a.id AS id,
               a.pet.name AS petName,
               a.doctor.lastName AS doctorLastName,
               s.dateTime AS dateTime,
               a.reason AS reason
        FROM Appointment a
        JOIN a.slot s
        WHERE a.pet.owner = :owner
          AND s.dateTime > :dateTime
        ORDER BY s.dateTime ASC
    """)
    List<AppointmentsPageProjection> findUpcomingAppointments(
            @Param("owner") PetUser owner,
            @Param("dateTime") LocalDateTime dateTime
    );
}
