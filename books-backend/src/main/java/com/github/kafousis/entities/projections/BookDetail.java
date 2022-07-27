package com.github.kafousis.entities.projections;

import com.github.kafousis.entities.Author;
import com.github.kafousis.entities.Book;
import com.github.kafousis.entities.Genre;
import com.github.kafousis.entities.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

@Projection(name = "bookDetail", types = {Book.class})
public interface BookDetail {
    String getTitle();
    String getIsbn();
    Integer getTotalPages();
    Integer getPublishedYear();
    Publisher getPublisher();
    Genre getGenre();
    @Value("#{target.authors.size()}")
    Integer getAuthorsCount();
    Set<Author> getAuthors();

    @Value("#{target.authors.size() == 2 ? target.authors[0].fullName + ', ' + target.authors[1].fullName : target.authors[0].fullName}")
    String getAllAuthors();
}
