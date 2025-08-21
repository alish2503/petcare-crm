package com.vet_care.demo.dto;

import java.time.LocalDateTime;

/**
 * @author Alish
 */
public record FlatDoctorSlotDto(Long doctorId,
                                String lastName,
                                String specialization,
                                Long slotId,
                                LocalDateTime dateTime,
                                boolean booked
) {}
