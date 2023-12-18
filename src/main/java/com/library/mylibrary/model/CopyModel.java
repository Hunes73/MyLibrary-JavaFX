package com.library.mylibrary.model;

import com.library.mylibrary.data.entity.Book;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CopyModel {

    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @NonNull
    private Book book;

    @NonNull
    private StringProperty yearOfPublication;

    @NonNull
    private StringProperty language;

    private StringProperty publisher;

    @NonNull
    private StringProperty isbn;

    @NonNull
    private StringProperty status;

    public CopyModel(Long id, @NonNull Book book, @NonNull String yearOfPublication, @NonNull String language,
                     String publisher, @NonNull String isbn, @NonNull String status) {
        this.id = id;
        this.book = book;
        this.yearOfPublication = new SimpleStringProperty(yearOfPublication);
        this.language = new SimpleStringProperty(language);
        this.publisher = new SimpleStringProperty(publisher);
        this.isbn = new SimpleStringProperty(isbn);
        this.status = new SimpleStringProperty(status);
    }
}
