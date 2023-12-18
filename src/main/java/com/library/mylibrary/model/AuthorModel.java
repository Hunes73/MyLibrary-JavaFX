package com.library.mylibrary.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorModel {

    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @NonNull
    private StringProperty firstname;

    @NonNull
    private StringProperty lastname;

    @NonNull
    private IntegerProperty yearOfBirth;

    private StringProperty country;

    public AuthorModel(@NonNull Long id, @NonNull String firstname, @NonNull String lastname, @NonNull Integer yearOfBirth, String country) {
        this.id = id;
        this.firstname = new SimpleStringProperty(firstname);
        this.lastname = new SimpleStringProperty(lastname);
        this.yearOfBirth = new SimpleIntegerProperty(yearOfBirth);
        this.country = new SimpleStringProperty(country);
    }
}
