package com.github.kafousis.repositories;

import com.github.kafousis.entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    boolean existsPublisherByName(String name);
}
