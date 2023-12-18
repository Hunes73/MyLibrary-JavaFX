package com.library.mylibrary.model;

import com.library.mylibrary.data.entity.Author;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookModel {

    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @NonNull
    private StringProperty title;

    private StringProperty genre;

    private StringProperty description;

    private List<Author> authors;

    public BookModel(@NonNull Long id, @NonNull String title, String genre, String description, List<Author> authors) {
        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.genre = new SimpleStringProperty(genre);
        this.description = new SimpleStringProperty(description);
        this.authors = authors;
    }
}
