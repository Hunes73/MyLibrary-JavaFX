package com.library.mylibrary.controller;

import com.library.mylibrary.data.enums.Role;
import com.library.mylibrary.model.CopyModel;
import com.library.mylibrary.service.impl.CopyServiceImpl;
import javafx.beans.property.SimpleStringProperty;
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
public class CopyController implements Initializable {

    private final MainController mainController;

    private final CopyServiceImpl copyService;

    private final CopyDetailsController copyDetailsController;

    @FXML
    public Button addButton, editButton, deleteButton;

    @FXML
    public TableView<CopyModel> tableView;

    @FXML
    public TableColumn<CopyModel, String> titleColumn, languageColumn, publisherColumn, isbnColumn, yearColumn;

    public CopyController(MainController mainController, CopyServiceImpl copyService, CopyDetailsController copyDetailsController) {
        this.mainController = mainController;
        this.copyService = copyService;
        this.copyDetailsController = copyDetailsController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (mainController.getLoggedUser().getRole().equals(Role.READER)) {
            addButton.setVisible(false);
            editButton.setVisible(false);
            deleteButton.setVisible(false);
        }
        tableView.setItems(copyService.getAllCopies());
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBook().getTitle()));
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().getYearOfPublication());
        languageColumn.setCellValueFactory(cellData -> cellData.getValue().getLanguage());
        publisherColumn.setCellValueFactory(cellData -> cellData.getValue().getPublisher());
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().getIsbn());
    }

    public void addCopy() throws IOException {
        copyDetailsController.setMode("create");
        mainController.switchCenterPane("/fxml/copy_details.fxml");
    }

    public void deleteCopy() throws IOException {
        CopyModel copyModel = tableView.getSelectionModel().getSelectedItem();
        if (copyModel == null) {
            mainController.showPopup("Nie wybrano egzemplaża");
            return;
        }
        copyService.deleteCopy(copyModel.getId());
        tableView.setItems(copyService.getAllCopies());
    }

    public void editCopy() throws IOException {
        CopyModel copyModel = tableView.getSelectionModel().getSelectedItem();
        if (copyModel == null) {
            mainController.showPopup("Nie wybrano egzemplaża");
            return;
        }
        copyDetailsController.setMode("update");
        copyDetailsController.setCopyModel(copyModel);
        mainController.switchCenterPane("/fxml/copy_details.fxml");
    }
}
