package com.library.mylibrary.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class PopupController implements Initializable {

    @Setter
    private String message;

    @FXML
    private Label label;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.label.setText(message);
    }

    public void close(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }
}
