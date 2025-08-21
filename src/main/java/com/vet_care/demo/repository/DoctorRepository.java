package com.vet_care.demo.repository;

import com.vet_care.demo.dto.FlatDoctorSlotDto;
import com.vet_care.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("""
        SELECT new com.vet_care.demo.dto.FlatDoctorSlotDto(
            d.id,
            d.lastName,
            d.specialization,
            s.id,
            s.dateTime,
            s.booked
        )
        FROM Doctor d
        JOIN d.availableSlots s
        WHERE s.dateTime > :now
        ORDER BY d.id, s.dateTime
        """)
    List<FlatDoctorSlotDto> findAllFutureDoctorSlotsFlat(@Param("now") LocalDateTime now);

}
