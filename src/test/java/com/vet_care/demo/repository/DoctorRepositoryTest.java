package com.vet_care.demo.repository;

import com.vet_care.demo.dto.FlatDoctorSlotDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alish
 */
@DataJpaTest
@ActiveProfiles("test")
public class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void findAllFutureDoctorSlotsFlat_shouldReturnOnlyFutureSlots() {
        LocalDateTime now = LocalDateTime.now();

        List<FlatDoctorSlotDto> result = doctorRepository.findAllFutureDoctorSlotsFlat(now);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        for (FlatDoctorSlotDto slot : result) {
            assertTrue(slot.dateTime().isAfter(now));
            assertNotNull(slot.doctorId());
        }
    }
}


