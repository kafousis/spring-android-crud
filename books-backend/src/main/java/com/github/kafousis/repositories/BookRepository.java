package com.github.kafousis.repositories;

import com.github.kafousis.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsBookByTitle(String title);
    boolean existsBookByIsbn(String isbn);
}
