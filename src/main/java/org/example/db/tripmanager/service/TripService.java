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
        validate(request);
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

    public List<TripResponse> findAll(String destination) {
        return repository.findAll()
                .stream()
                .filter(trip -> destination == null || destination.equals(trip.getDestination()))
                .map(this::toResponse)
                .toList();
    }
    public void delete(Long id) {
        findTrip(id);
        repository.deleteById(id);
    }

    public TripResponse update(Long id, TripRequest request) {
        Trip trip = findTrip(id);
        validate(request);
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
    private void validate(TripRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Title is required"
            );
        }
        if(request.budget() == null || request.budget() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Budget must be zero or greater than zero");
        }
        if(request.destination() == null || request.destination().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Destination is required");
        }
        if (request.startDate() == null || request.endDate() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start date is required"
            );
        }
        if(request.endDate().isBefore(request.startDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date is before start date");
        }
    }
}

