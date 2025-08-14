package com.vet_care.demo.repository;

import com.vet_care.demo.dto.DoctorsPageProjection;
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
    select d.id as id,
           d.lastName as lastName,
           d.specialization as specialization,
           s.id as availableSlots_id,
           s.dateTime as availableSlots_dateTime,
           s.booked as availableSlots_booked
    from Doctor d
    join d.availableSlots s
    where s.dateTime > :now
    order by s.dateTime asc
""")
    List<DoctorsPageProjection> findDoctorsWithAvailableFutureSlots(@Param("now") LocalDateTime now);
}
