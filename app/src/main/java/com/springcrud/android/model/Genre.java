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
public class Genre {
    private Long id;
    private String name;
    private Set<Book> books;
}
