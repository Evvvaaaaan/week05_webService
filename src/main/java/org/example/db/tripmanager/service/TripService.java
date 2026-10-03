package org.example.db.tripmanager.service;


import org.example.db.tripmanager.domain.Trip;
import org.example.db.tripmanager.dto.TripRequest;
import org.example.db.tripmanager.dto.TripResponse;
import org.example.db.tripmanager.repository.TripRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public void delete(Long id) {
        findTrip(id);
        repository.deleteById(id);
    }

    public TripResponse update(Long id, TripRequest request) {
        Trip trip = findTrip(id);
        trip.setTitle(request.title());
        trip.setDestination(request.destination());
        trip.setStartDate(request.startDate());
        trip.setEndDate(request.endDate());
        trip.setBudget(request.budget());
        trip.setMemo(request.memo());

        return toResponse(repository.update(trip));
    }
    public TripResponse findById(Long id) {
        Trip trip = findTrip(id);
        return toResponse(trip);
    }

    private Trip findTrip(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Trip not found: " + id

        ));
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

