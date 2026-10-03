package org.example.db.tripmanager.dto;

import java.time.LocalDate;

public record TripRequest(
        String title,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        Integer budget,
        String memo
) {

}
