package com.github.kafousis.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "books")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode @ToString @Accessors(chain = true)
@SequenceGenerator(name = "book_generator", sequenceName = "book_id_seq", allocationSize = 1)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator")
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, unique = true, length = 33)
    private String isbn;

    @Column(nullable = false)
    private Integer totalPages;

    @Column(nullable = false)
    private Integer publishedYear;

    @ManyToOne
    @JoinColumn(nullable = false, name = "publisher_id")
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(nullable = false, name = "genre_id")
    private Genre genre;

    @ManyToMany
    @JoinTable(
            name = "books_authors",
            joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "author_id") }
    )
    private Set<Author> authors = new HashSet<>();
}
