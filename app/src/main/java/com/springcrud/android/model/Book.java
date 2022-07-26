package com.springcrud.android.model;

import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode @ToString @Accessors(chain = true)
public class Book {
    private Long id;
    private String title;
    private String isbn;
    private Integer totalPages;
    private Integer publishedYear;
    private Publisher publisher;
    private Genre genre;
    private Set<Author> authors;
}
