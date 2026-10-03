package repository;


import domain.Book;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository {
    Book save(Book b);
    List<Book> findAll();
    Optional<Book> findById();

}
