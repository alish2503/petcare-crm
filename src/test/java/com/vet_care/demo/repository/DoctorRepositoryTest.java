package com.vet_care.demo.repository;

import com.vet_care.demo.dto.FlatDoctorSlotDto;
import com.vet_care.demo.model.AvailableSlot;
import com.vet_care.demo.model.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alish
 */
@DataJpaTest
public class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AvailableSlotRepository slotRepository;

    @Test
    void findAllFutureDoctorSlotsFlat_shouldReturnOnlyFutureSlotsWithCorrectDTO() {
        LocalDateTime now = LocalDateTime.now();
        Doctor doc1 = new Doctor("Alice", "Smith", "alice@vet.com",
                "not_used", "Cardiology");

        Doctor doc2 = new Doctor("Bob", "Brown", "bob@vet.com",
                "not_used", "Dermatology");

        doctorRepository.save(doc1);
        doctorRepository.save(doc2);
        AvailableSlot pastSlot = new AvailableSlot(now.minusDays(1), false, doc1);
        AvailableSlot futureSlot1 = new AvailableSlot(now.plusDays(1), false, doc1);
        AvailableSlot futureSlot2 = new AvailableSlot(now.plusDays(2), true, doc2);
        slotRepository.saveAll(List.of(pastSlot, futureSlot1, futureSlot2));
        List<FlatDoctorSlotDto> result = doctorRepository.findAllFutureDoctorSlotsFlat(now);
        assertThat(result).hasSize(2);
        assertThat(result).extracting("doctorId").containsExactly(doc1.getId(), doc2.getId());
        assertThat(result).extracting("lastName").containsExactly("Smith", "Brown");
        assertThat(result).extracting("specialization").containsExactly("Cardiology", "Dermatology");
        assertThat(result).extracting("slotId").containsExactly(futureSlot1.getId(), futureSlot2.getId());
        assertThat(result).extracting("booked").containsExactly(false, true);
        assertThat(result.get(0).dateTime()).isBefore(result.get(1).dateTime());
    }
}
