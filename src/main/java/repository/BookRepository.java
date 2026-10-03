package repository;


import org.example.db.tripmanager.domain.Trip;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository {
    Trip save(Trip b);
    List<Trip> findAll();
    Optional<Trip> findById();

}
