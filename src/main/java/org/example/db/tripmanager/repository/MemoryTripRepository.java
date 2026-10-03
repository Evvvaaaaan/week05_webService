package org.example.db.tripmanager.repository;

import org.example.db.tripmanager.domain.Trip;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryTripRepository implements TripRepository {

    private final Map<Long, Trip> store = new LinkedHashMap<>();
    private long sequence = 0L;

    @Override
    public Trip save(Trip trip) {
        trip.setId(++sequence);
        store.put(trip.getId(), trip);
        return trip;
    }

    @Override
    public List<Trip> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Trip> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Trip update(Trip trip) {
        store.put(trip.getId(), trip);
        return trip;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

}
