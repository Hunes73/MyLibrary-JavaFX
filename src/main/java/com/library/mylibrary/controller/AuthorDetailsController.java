package com.library.mylibrary.controller;

import com.library.mylibrary.model.AuthorModel;
import com.library.mylibrary.service.impl.AuthorServiceImpl;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class AuthorDetailsController implements Initializable {

    private final MainController mainController;

    private final AuthorServiceImpl authorService;

    @Setter
    private AuthorModel authorModel;

    @Setter
    private String mode;

    @FXML
    private Label title;

    @FXML
    private TextField firstnameField, surnameField, yearOfBirthField, countryField;

    public AuthorDetailsController(MainController mainController, AuthorServiceImpl authorService) {
        this.mainController = mainController;
        this.authorService = authorService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switch (mode) {
            case "update" -> {
                title.setText("Edytuj autora");
                firstnameField.setText(authorModel.getFirstname().getValue());
                surnameField.setText(authorModel.getLastname().getValue());
                yearOfBirthField.setText(authorModel.getYearOfBirth().getValue().toString());
                countryField.setText(authorModel.getCountry().getValue());
            }
            case "create" -> title.setText("Utwórz autora");
        }
    }

    @FXML
    public void save() throws IOException {
        if (firstnameField.getText().isEmpty() || surnameField.getText().isEmpty() || yearOfBirthField.getText().isEmpty()) {
            mainController.showPopup("Wypełnij pola: Imię, Nazwisko, Rok urodzenia!");
            return;
        }
        if (!yearOfBirthField.getText().matches("[0-9]+")) {
            mainController.showPopup("Rok urodzenia musi być liczbą!");
            return;
        }
        if (authorModel == null) {
            authorModel = new AuthorModel();
        }
        authorModel.setFirstname(new SimpleStringProperty(firstnameField.getText()));
        authorModel.setLastname(new SimpleStringProperty(surnameField.getText()));
        authorModel.setYearOfBirth(new SimpleIntegerProperty(Integer.parseInt(yearOfBirthField.getText())));
        authorModel.setCountry(new SimpleStringProperty(countryField.getText()));

        switch (mode) {
            case "update" -> authorService.updateAuthor(authorModel);
            case "create" -> authorService.createAuthor(authorModel);
        }
        mainController.switchCenterPane("/fxml/authors.fxml");
    }

    @FXML
    public void cancel() throws IOException {
        mainController.switchCenterPane("/fxml/authors.fxml");
    }
}
