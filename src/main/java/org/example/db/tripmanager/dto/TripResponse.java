package org.example.db.tripmanager.dto;

import java.time.LocalDate;

public record TripResponse(
        Long id,
        String title,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        Integer budget,
        String memo
) {
}
