package com.library.mylibrary.controller;

import com.library.mylibrary.model.BorrowerModel;
import com.library.mylibrary.service.impl.UserServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Setter;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class BorrowerDetailsController implements Initializable {

    private final MainController mainController;

    private final UserServiceImpl userService;

    @Setter
    private BorrowerModel borrowerModel;

    @Setter
    private String mode;

    @FXML
    private Label title;

    @FXML
    private TextField cardNumberField, firstnameField, lastnameField, emailField, phoneField, passwordField;

    @FXML
    public VBox passwordBox;

    public BorrowerDetailsController(MainController mainController, UserServiceImpl userService) {
        this.mainController = mainController;
        this.userService = userService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switch (mode) {
            case "update" -> {
                title.setText("Edytuj czytelnika");
                cardNumberField.setText(borrowerModel.getCardNumber().get());
                firstnameField.setText(borrowerModel.getFirstname().getValue());
                lastnameField.setText(borrowerModel.getLastname().getValue());
                emailField.setText(borrowerModel.getEmail().getValue());
                phoneField.setText(borrowerModel.getPhone().getValue());
                passwordBox.setVisible(false);
            }
            case "create" -> {
                title.setText("Utwórz czytelnika");
            }
        }
    }

    @FXML
    public void save() throws IOException {
        if (firstnameField.getText().isEmpty() || lastnameField.getText().isEmpty() || cardNumberField.getText().isEmpty() || emailField.getText().isEmpty()) {
            mainController.showPopup("Wypełnij pola: Numer karty, Imię, Nazwisko, Email!");
            return;
        }
        if (!EmailValidator.getInstance().isValid(emailField.getText())) {
            mainController.showPopup("Niepoprawny adres email!");
            return;
        }

        if (borrowerModel == null) {
            borrowerModel = new BorrowerModel();
        }
        borrowerModel.setCardNumber(new SimpleStringProperty(cardNumberField.getText()));
        borrowerModel.setFirstname(new SimpleStringProperty(firstnameField.getText()));
        borrowerModel.setLastname(new SimpleStringProperty(lastnameField.getText()));
        borrowerModel.setEmail(new SimpleStringProperty(emailField.getText()));
        borrowerModel.setPhone(new SimpleStringProperty(phoneField.getText()));

        switch (mode) {
            case "update" -> userService.updateUser(borrowerModel);
            case "create" -> {
                if (passwordField.getText().isEmpty()) {
                    mainController.showPopup("Wypełnij pole: Hasło!");
                    return;
                }
                borrowerModel.setPassword(new SimpleStringProperty(passwordField.getText()));
                userService.registerNewUser(borrowerModel);
            }
        }
        mainController.switchCenterPane("/fxml/borrowers.fxml");
    }

    @FXML
    public void cancel() throws IOException {
        mainController.switchCenterPane("/fxml/borrowers.fxml");
    }
}
