package com.library.mylibrary.controller;

import com.library.mylibrary.data.enums.Role;
import com.library.mylibrary.model.AuthorModel;
import com.library.mylibrary.service.impl.AuthorServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


@Component
public class AuthorController implements Initializable {

    private final MainController mainController;

    private final AuthorServiceImpl authorService;

    private final AuthorDetailsController authorDetailsController;

    @FXML
    public Button addButton, editButton, deleteButton;

    @FXML
    private TableView<AuthorModel> tableView;

    @FXML
    private TableColumn<AuthorModel, String> firstnameColumn, lastnameColumn, countryColumn;

    @FXML
    private TableColumn<AuthorModel, Integer> yearOfBirthColumn;

    public AuthorController(MainController mainController, AuthorServiceImpl authorService, AuthorDetailsController authorDetailsController) {
        this.mainController = mainController;
        this.authorService = authorService;
        this.authorDetailsController = authorDetailsController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (mainController.getLoggedUser().getRole().equals(Role.READER)) {
            addButton.setVisible(false);
            editButton.setVisible(false);
            deleteButton.setVisible(false);
        }
        tableView.setItems(authorService.getAllAuthors());
        firstnameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstname());
        lastnameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastname());
        yearOfBirthColumn.setCellValueFactory(cellData -> cellData.getValue().getYearOfBirth().asObject());
        countryColumn.setCellValueFactory(cellData -> cellData.getValue().getCountry());
    }

    @FXML
    public void addAuthor() throws IOException {
        authorDetailsController.setMode("create");
        mainController.switchCenterPane("/fxml/author_details.fxml");
    }

    @FXML
    public void deleteAuthor() throws IOException {
        AuthorModel authorModel = tableView.getSelectionModel().getSelectedItem();
        if (authorModel == null) {
            mainController.showPopup("Nie wybrano autora");
            return;
        }
        authorService.deleteAuthor(authorModel.getId());
        tableView.setItems(authorService.getAllAuthors());
        mainController.switchCenterPane("/fxml/authors.fxml");
    }

    @FXML
    public void editAuthor() throws IOException {
        AuthorModel authorModel = tableView.getSelectionModel().getSelectedItem();
        if (authorModel == null) {
            mainController.showPopup("Nie wybrano autora");
            return;
        }
        authorDetailsController.setAuthorModel(authorModel);
        authorDetailsController.setMode("update");
        mainController.switchCenterPane("/fxml/author_details.fxml");
    }
}
