package com.vet_care.demo.repository;

import com.vet_care.demo.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alish
 */
@Repository
public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
    @Query("SELECT DISTINCT v FROM Veterinarian v JOIN FETCH v.availableSlots s WHERE s.dateTime > CURRENT_TIMESTAMP")
    List<Veterinarian> findAllWithFutureSlots();

}
