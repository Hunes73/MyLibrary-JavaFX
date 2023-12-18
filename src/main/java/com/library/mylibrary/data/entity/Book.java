package com.library.mylibrary.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String title;

    private String genre;

    private String description;

    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER)
    private List<Author> authors = List.of();

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private List<Copy> copies = List.of();

    public Book(@NonNull String title, String genre, String description, List<Author> authors) {
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.authors = authors;
    }
}
