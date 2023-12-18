package com.library.mylibrary.model;

import com.library.mylibrary.data.entity.Copy;
import com.library.mylibrary.data.entity.User;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class LoanModel {

    @Setter(lombok.AccessLevel.NONE)
    private LongProperty id;

    @NonNull
    private Copy copy;

    @NonNull
    private User user;

    @NonNull
    private StringProperty title;

    @NonNull
    private StringProperty cardNumber;

    @NonNull
    private StringProperty fullName;

    @NonNull
    private LocalDate loanDate;

    @NonNull
    private LocalDate plannedReturnDate;

    private LocalDate actualReturnDate;

    public LoanModel(@NonNull Long id, @NonNull Copy copy, @NonNull User user, @NonNull String title, @NonNull String cardNumber,
                     @NonNull String fullName, @NonNull LocalDate loanDate, @NonNull LocalDate plannedReturnDate, LocalDate actualReturnDate) {
        this.id = new SimpleLongProperty(id);
        this.copy = copy;
        this.user = user;
        this.title = new SimpleStringProperty(title);
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.fullName = new SimpleStringProperty(fullName);
        this.loanDate = loanDate;
        this.plannedReturnDate = plannedReturnDate;
        this.actualReturnDate = actualReturnDate;
    }
}
