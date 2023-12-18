package com.library.mylibrary.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NonNull
    private String firstname;

    @Column(name = "last_name", nullable = false)
    @NonNull
    private String lastname;

    @Column(name = "year_of_birth", nullable = false)
    @NonNull
    private Integer yearOfBirth;

    private String country;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();

    public Author(@NonNull String firstname, @NonNull String lastname, @NonNull Integer yearOfBirth, String country) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.yearOfBirth = yearOfBirth;
        this.country = country;
    }
}
