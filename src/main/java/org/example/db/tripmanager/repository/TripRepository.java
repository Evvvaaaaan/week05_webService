package org.example.db.tripmanager.repository;


import org.example.db.tripmanager.domain.Trip;

import java.util.List;
import java.util.Optional;

public interface TripRepository {
    Trip save(Trip b);
    List<Trip> findAll();
    Optional<Trip> findById(Long id);
    Trip update(Trip trip);
    void deleteById(Long id);

}
