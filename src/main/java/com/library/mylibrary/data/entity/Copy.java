package com.library.mylibrary.data.entity;

import com.library.mylibrary.data.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "copies")
public class Copy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @NonNull
    private Book book;

    @Column(nullable = false)
    @NonNull
    private String isbn;

    @Column(name = "year_of_publication", nullable = false)
    @NonNull
    private String yearOfPublication;

    private String publisher;

    @Column(nullable = false)
    @NonNull
    private String language;

    @Column(nullable = false)
    @NonNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "copy")
    private List<Loan> loans;

    public Copy(@NonNull Book book, @NonNull String isbn, @NonNull String yearOfPublication, String publisher,
                @NonNull String language, @NonNull Status status) {
        this.book = book;
        this.isbn = isbn;
        this.yearOfPublication = yearOfPublication;
        this.publisher = publisher;
        this.language = language;
        this.status = status;
    }
}
