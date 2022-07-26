package com.github.kafousis.repositories;

import com.github.kafousis.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsAuthorByFullName(String fullName);
}
