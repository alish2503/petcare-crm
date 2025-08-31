package com.vet_care.demo.dto;

import com.vet_care.demo.model.Specialization;

import java.time.LocalDateTime;

/**
 * @author Alish
 */
public record FlatDoctorSlotDto(Long doctorId,
                                String lastName,
                                Specialization specialization,
                                Long slotId,
                                LocalDateTime dateTime,
                                boolean booked
) {}
