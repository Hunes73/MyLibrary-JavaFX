package com.library.mylibrary.controller;

import com.library.mylibrary.model.BorrowerModel;
import com.library.mylibrary.service.impl.UserServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class BorrowerController implements Initializable {

    private final MainController mainController;

    private final UserServiceImpl userService;

    private final BorrowerDetailsController borrowerDetailsController;

    @FXML
    private TableView<BorrowerModel> tableView;

    @FXML
    private TableColumn<BorrowerModel, String> firstnameColumn, lastnameColumn, cardNumberColumn, emailColumn, phoneColumn;

    public BorrowerController(MainController mainController, UserServiceImpl userService, BorrowerDetailsController borrowerDetailsController) {
        this.mainController = mainController;
        this.userService = userService;
        this.borrowerDetailsController = borrowerDetailsController;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableView.setItems(userService.getAllReaders());
        firstnameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstname());
        lastnameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastname());
        cardNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getCardNumber());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmail());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().getPhone());
    }

    @FXML
    public void addBorrower() throws IOException {
        borrowerDetailsController.setMode("create");
        mainController.switchCenterPane("/fxml/borrower_details.fxml");
    }

    @FXML
    public void deleteBorrower() throws IOException {
        BorrowerModel borrowerModel = tableView.getSelectionModel().getSelectedItem();
        if (borrowerModel == null) {
            mainController.showPopup("Nie wybrano czytelnika");
            return;
        }
        userService.deleteUser(borrowerModel.getId());
        tableView.setItems(userService.getAllReaders());
    }

    @FXML
    public void editBorrower() throws IOException {
        BorrowerModel borrowerModel = tableView.getSelectionModel().getSelectedItem();
        if (borrowerModel == null) {
            mainController.showPopup("Nie wybrano czytelnika");
            return;
        }
        borrowerDetailsController.setBorrowerModel(borrowerModel);
        borrowerDetailsController.setMode("update");
        mainController.switchCenterPane("/fxml/borrower_details.fxml");
    }
}
