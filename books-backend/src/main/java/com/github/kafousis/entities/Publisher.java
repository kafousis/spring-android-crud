package com.github.kafousis.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "publishers")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode @ToString @Accessors(chain = true)
@SequenceGenerator(name = "publisher_generator", sequenceName = "publisher_id_seq", allocationSize = 1)
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publisher_generator")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String country;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "publisher")
    private Set<Book> books = new HashSet<>();
}
