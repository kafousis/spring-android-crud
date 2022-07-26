package com.github.kafousis.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "genres")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode @ToString @Accessors(chain = true)
@SequenceGenerator(name = "genre_generator", sequenceName = "genre_id_seq", allocationSize = 1)
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_generator")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "genre")
    private Set<Book> books = new HashSet<>();
}
