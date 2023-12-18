package com.library.mylibrary.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BorrowerModel {

    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @NonNull
    private StringProperty cardNumber;

    @NonNull
    private StringProperty firstname;

    @NonNull
    private StringProperty lastname;

    @NonNull
    private StringProperty email;

    private StringProperty phone;

    private StringProperty password;

    public BorrowerModel(@NonNull Long id, @NonNull String cardNumber, @NonNull String firstname, @NonNull String lastname, @NonNull String email, String phone) {
        this.id = id;
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.firstname = new SimpleStringProperty(firstname);
        this.lastname = new SimpleStringProperty(lastname);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
    }
}
