package com.github.kafousis.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "authors")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode @ToString @Accessors(chain = true)
@SequenceGenerator(name = "author_generator", sequenceName = "author_id_seq", allocationSize = 1)
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_generator")
    private Long id;

    @Column(nullable = false, unique = true)
    private String fullName;

    @Column(nullable = false)
    private String country;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();
}
