package com.vet_care.demo.repository;

import com.vet_care.demo.dto.AppointmentsPageProjection;
import com.vet_care.demo.model.Appointment;
import com.vet_care.demo.model.Doctor;
import com.vet_care.demo.model.Pet;
import com.vet_care.demo.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        SELECT a.id as id,
               a.reason as reason,
               p.name as petName,
               d.lastName as otherPartyName,
               s.slotTime as slotDateTime
        FROM Appointment a
        JOIN a.pet p
        JOIN a.doctor d
        JOIN a.slot s
        WHERE p.owner = :owner
        ORDER BY s.slotTime ASC
    """)
    List<AppointmentsPageProjection> findAppointmentsForOwner(@Param("owner") PetOwner owner);

    @Query("""
        SELECT a.id as id,
               a.reason as reason,
               p.name as petName,
               o.lastName as otherPartyName,
               s.slotTime as slotDateTime
        FROM Appointment a
        JOIN a.pet p
        JOIN p.owner o
        JOIN a.slot s
        WHERE a.doctor = :doctor
        ORDER BY s.slotTime ASC
    """)
    List<AppointmentsPageProjection> findAppointmentsForDoctor(@Param("doctor") Doctor doctor);
    List<Appointment> findAllByPet(Pet pet);

    boolean existsBySlotSlotTimeAndDoctor(LocalDateTime dateTime, Doctor doctor);
    boolean existsByDoctorAndPetId(Doctor doctor, Long petId);
}
