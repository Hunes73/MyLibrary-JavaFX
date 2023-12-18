package com.library.mylibrary.controller;

import com.library.mylibrary.data.enums.Role;
import com.library.mylibrary.model.LoanModel;
import com.library.mylibrary.service.impl.LoanServiceImpl;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

@Component
public class LoanController implements Initializable {

    private final MainController mainController;

    private final LoanDetailsController loanDetailsController;

    private final LoanServiceImpl loanService;

    @FXML
    public Label title;

    @FXML
    public HBox topBox;

    @FXML
    public Button addButton, editButton, deleteButton, returnButton;

    @FXML
    public TableView<LoanModel> tableView;

    @FXML
    public TableColumn<LoanModel, String> titleColumn, fullNameColumn, cardNumberColumn;

    @FXML
    public TableColumn<LoanModel, Long> idColumn;

    @FXML
    public TableColumn<LoanModel, LocalDate> loanDateColumn, plannedReturnDateColumn, actualReturnDateColumn;

    public LoanController(MainController mainController, LoanDetailsController loanDetailsController, LoanServiceImpl loanService) {
        this.mainController = mainController;
        this.loanDetailsController = loanDetailsController;
        this.loanService = loanService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Node> children = topBox.getChildren();
        if (mainController.getLoggedUser().getRole().equals(Role.READER)) {
            title.setText("Moje wypożyczenia");
            children.remove(addButton);
            children.remove(editButton);
            children.remove(deleteButton);
            tableView.getColumns().removeAll(fullNameColumn, cardNumberColumn);
            tableView.setItems(loanService.getAllLoansByUser(mainController.getLoggedUser().getId()));
        } else {
            children.remove(returnButton);
            tableView.setItems(loanService.getAllLoans());
        }
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitle());
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        fullNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFullName());
        cardNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getCardNumber());
        loanDateColumn.setCellFactory(column -> new DateCell());
        plannedReturnDateColumn.setCellFactory(column -> new DateCell());
        actualReturnDateColumn.setCellFactory(column -> new DateCell());
    }

    @FXML
    public void addLoan() throws IOException {
        loanDetailsController.setMode("create");
        mainController.switchCenterPane("/fxml/loan_details.fxml");
    }

    @FXML
    public void deleteLoan() throws IOException {
        LoanModel loanModel = tableView.getSelectionModel().getSelectedItem();
        if (loanModel == null) {
            mainController.showPopup("Nie wybrano wypożyczenia");
            return;
        }
        loanService.deleteLoan(loanModel.getId().getValue());
        tableView.setItems(loanService.getAllLoans());
    }

    @FXML
    public void editLoan() throws IOException {
        LoanModel loanModel = tableView.getSelectionModel().getSelectedItem();
        if (loanModel == null) {
            mainController.showPopup("Nie wybrano wypożyczenia");
            return;
        }
        loanDetailsController.setMode("update");
        loanDetailsController.setLoanModel(loanModel);
        mainController.switchCenterPane("/fxml/loan_details.fxml");
    }

    public void returnBook() throws IOException {
        LoanModel loanModel = tableView.getSelectionModel().getSelectedItem();
        if (loanModel == null) {
            mainController.showPopup("Nie wybrano wypożyczenia");
            return;
        }
        loanService.returnBook(loanModel.getId().getValue());
        tableView.setItems(loanService.getAllLoans());
    }

    public static class DateCell extends TableCell<LoanModel, LocalDate> {
        @Override
        protected void updateItem(LocalDate date, boolean empty) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            super.updateItem(date, empty);
            if (date == null || empty) {
                setText("");
            } else {
                setText(formatter.format(date));
            }
        }
    }
}
