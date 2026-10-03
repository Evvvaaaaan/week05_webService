package org.example.db.tripmanager.service;


import org.example.db.tripmanager.domain.Trip;
import org.example.db.tripmanager.dto.TripRequest;
import org.example.db.tripmanager.dto.TripResponse;
import org.example.db.tripmanager.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {
    private final TripRepository repository;

    public TripService(TripRepository repository) {
        this.repository = repository;
    }

    public TripResponse create(TripRequest request) {
        Trip trip = new Trip(
                null,
                request.title(),
                request.destination(),
                request.startDate(),
                request.endDate(),
                request.budget(),
                request.memo()
        );

        Trip savedTrip = repository.save(trip);
        return toResponse(savedTrip);
    }

    public List<TripResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TripResponse toResponse(Trip trip) {
        return new TripResponse(
                trip.getId(),
                trip.getTitle(),
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getBudget(),
                trip.getMemo()
        );
    }
}

