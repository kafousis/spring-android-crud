package com.github.kafousis.repositories;

import com.github.kafousis.entities.Book;
import com.github.kafousis.entities.projections.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsBookByTitle(String title);
    boolean existsBookByIsbn(String isbn);
}
