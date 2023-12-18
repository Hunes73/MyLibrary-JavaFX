package com.library.mylibrary.controller;

import com.library.mylibrary.data.enums.Status;
import com.library.mylibrary.model.BorrowerModel;
import com.library.mylibrary.model.CopyModel;
import com.library.mylibrary.model.LoanModel;
import com.library.mylibrary.service.impl.CopyServiceImpl;
import com.library.mylibrary.service.impl.LoanServiceImpl;
import com.library.mylibrary.service.impl.UserServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Component
public class LoanDetailsController implements Initializable {

    private final MainController mainController;

    private final LoanServiceImpl loanService;

    private final CopyServiceImpl copyService;

    private final UserServiceImpl userService;
    @FXML
    public ComboBox<CopyModel> copyComboBox;

    @FXML
    public ComboBox<BorrowerModel> borrowerComboBox;

    @FXML
    public DatePicker loanDatePicker, plannedReturnDatePicker, actualReturnDatePicker;

    @Setter
    private String mode;

    @Setter
    private LoanModel loanModel;

    @FXML
    private Label title;


    public LoanDetailsController(MainController mainController, LoanServiceImpl loanService, CopyServiceImpl copyService, UserServiceImpl userService) {
        this.mainController = mainController;
        this.loanService = loanService;
        this.copyService = copyService;
        this.userService = userService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        copyComboBox.setCellFactory(param -> new CopyCell());
        copyComboBox.setButtonCell(new CopyCell());
        copyComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(CopyModel copyModel) {
                if (copyModel == null)
                    return null;
                return "(" + copyModel.getId() + ") " + copyModel.getBook().getTitle();
            }

            @Override
            public CopyModel fromString(String s) {
                return null;
            }
        });

        borrowerComboBox.setCellFactory(param -> new BorrowerCell());
        borrowerComboBox.setButtonCell(new BorrowerCell());
        borrowerComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(BorrowerModel borrowerModel) {
                if (borrowerModel == null)
                    return null;
                return "(" + borrowerModel.getCardNumber().getValue() + ") " + borrowerModel.getFirstname().getValue()
                        + " " + borrowerModel.getLastname().getValue();
            }

            @Override
            public BorrowerModel fromString(String s) {
                return null;
            }
        });

        copyComboBox.setItems(copyService.getAvailableCopies());
        borrowerComboBox.setItems(userService.getAllReaders());

        switch (mode) {
            case "update" -> {
                title.setText("Edytuj wyporzycznie");
                copyComboBox.getSelectionModel().select(copyService.mapToModel(loanModel.getCopy()));
                borrowerComboBox.getSelectionModel().select(userService.mapToModel(loanModel.getUser()));
                loanDatePicker.setValue(loanModel.getLoanDate());
                plannedReturnDatePicker.setValue(loanModel.getPlannedReturnDate());
                actualReturnDatePicker.setValue(loanModel.getActualReturnDate());
            }
            case "create" -> {
                title.setText("Dodaj wyporzycznie");
                loanDatePicker.setValue(LocalDate.now());
                plannedReturnDatePicker.setValue(LocalDate.now().plusDays(14));
            }
        }
    }

    @FXML
    private void save() throws IOException {
        if (mode.equals("create") && (copyComboBox.getSelectionModel().isEmpty() || borrowerComboBox.getSelectionModel().isEmpty()
                || loanDatePicker.getValue() == null || plannedReturnDatePicker.getValue() == null)) {
            mainController.showPopup("Pola egzemplaż, czytelnik, data wypożycznia oraz data planowanego zwrotu nie mogą być puste!");
            return;
        } else if (mode.equals("update") && (loanDatePicker.getValue() == null || plannedReturnDatePicker.getValue() == null)) {
            mainController.showPopup("Pola data wypożycznia oraz data planowanego zwrotu nie mogą być puste!");
            return;
        }

        if (loanModel == null) {
            loanModel = new LoanModel();
        }
        loanModel.setCopy(copyService.getCopyById(copyComboBox.getSelectionModel().getSelectedItem().getId()));
        loanModel.setUser(userService.getUserById(borrowerComboBox.getSelectionModel().getSelectedItem().getId()));
        loanModel.setLoanDate(loanDatePicker.getValue());
        loanModel.setPlannedReturnDate(plannedReturnDatePicker.getValue());
        loanModel.setActualReturnDate(actualReturnDatePicker.getValue());
        if (actualReturnDatePicker.getValue() == null) {
            loanModel.getCopy().setStatus(Status.BORROWED);
        }

        switch (mode) {
            case "update" -> loanService.updateLoan(loanModel);
            case "create" -> loanService.createLoan(loanModel);
        }
        mainController.switchCenterPane("/fxml/loans.fxml");
    }

    @FXML
    private void cancel() throws IOException {
        mainController.switchCenterPane("/fxml/loans.fxml");
    }

    public static class CopyCell extends ListCell<CopyModel> {
        @Override
        protected void updateItem(CopyModel copyModel, boolean empty) {
            super.updateItem(copyModel, empty);
            if (copyModel == null || empty) {
                setText(null);
            } else {
                setText("(" + copyModel.getId() + ") " + copyModel.getBook().getTitle());
            }
        }
    }

    public static class BorrowerCell extends ListCell<BorrowerModel> {
        @Override
        protected void updateItem(BorrowerModel borrowerModel, boolean empty) {
            super.updateItem(borrowerModel, empty);
            if (borrowerModel == null || empty) {
                setText(null);
            } else {
                setText("(" + borrowerModel.getCardNumber().getValue() + ") " + borrowerModel.getFirstname().getValue()
                        + " " + borrowerModel.getLastname().getValue());
            }
        }
    }
}
