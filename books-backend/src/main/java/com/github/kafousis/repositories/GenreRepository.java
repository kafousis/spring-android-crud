package com.github.kafousis.repositories;

import com.github.kafousis.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsGenreByName(String name);
}
