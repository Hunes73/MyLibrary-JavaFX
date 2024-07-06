package com.library.mylibrary.controller;

import com.library.mylibrary.model.BookModel;
import com.library.mylibrary.model.CopyModel;
import com.library.mylibrary.service.impl.BookServiceImpl;
import com.library.mylibrary.service.impl.CopyServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class CopyDetailsController implements Initializable {

    private final MainController mainController;

    private final CopyServiceImpl copyService;

    private final BookServiceImpl bookService;

    @Setter
    private String mode;

    @Setter
    private CopyModel copyModel;

    @FXML
    private Label title;

    @FXML
    private TextField languageField, yearField, publisherField, isbnField;

    @FXML
    private ComboBox<BookModel> bookComboBox;


    public CopyDetailsController(MainController mainController, CopyServiceImpl copyService, BookServiceImpl bookService) {
        this.mainController = mainController;
        this.copyService = copyService;
        this.bookService = bookService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookComboBox.setItems(bookService.getAllBooks());
        bookComboBox.setCellFactory(param -> new BookCell());
        bookComboBox.setButtonCell(new BookCell());
        bookComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(BookModel bookModel) {
                if (bookModel == null)
                    return null;
                return bookModel.getTitle().getValue();
            }

            @Override
            public BookModel fromString(String s) {
                return null;
            }
        });

        switch (mode) {
            case "update" -> {
                title.setText("Edytuj egzemplarz");
                bookComboBox.getSelectionModel().select(bookService.mapToModel(copyModel.getBook()));
                yearField.setText(copyModel.getYearOfPublication().getValue());
                languageField.setText(copyModel.getLanguage().getValue());
                publisherField.setText(copyModel.getPublisher().getValue());
                isbnField.setText(copyModel.getIsbn().getValue());
            }
            case "create" -> {
                title.setText("Dodaj egzemplarz");
            }
        }
    }

    @FXML
    private void save() throws IOException {
        if (bookComboBox.getSelectionModel().isEmpty() || languageField.getText().isEmpty()
                || yearField.getText().isEmpty() || isbnField.getText().isEmpty() /*|| statusComboBox.getSelectionModel().isEmpty()*/) {
            mainController.showPopup("Pola książka, język, rok publikacji, ISBN oraz status nie mogą być puste!");
            return;
        }
        if (!yearField.getText().matches("[0-9]+")) {
            mainController.showPopup("Pole rok musi być liczbą!");
            return;
        }

        if (copyModel == null) {
            copyModel = new CopyModel();
        }
        copyModel.setBook(bookService.getBookById(bookComboBox.getSelectionModel().getSelectedItem().getId()));
        copyModel.setYearOfPublication(new SimpleStringProperty(yearField.getText()));
        copyModel.setLanguage(new SimpleStringProperty(languageField.getText()));
        copyModel.setPublisher(new SimpleStringProperty(publisherField.getText()));
        copyModel.setIsbn(new SimpleStringProperty(isbnField.getText()));
        copyModel.setStatus(new SimpleStringProperty("Dostępny"));
        switch (mode) {
            case "update" -> copyService.updateCopy(copyModel);
            case "create" -> copyService.createCopy(copyModel);
        }
        mainController.switchCenterPane("/fxml/copies.fxml");
    }

    @FXML
    private void cancel() throws IOException {
        mainController.switchCenterPane("/fxml/copies.fxml");
    }

    public static class BookCell extends ListCell<BookModel> {
        @Override
        public void updateItem(BookModel book, boolean empty) {
            super.updateItem(book, empty);

            if (book == null || empty) {
                setText(null);
            } else {
                setText(book.getTitle().getValue());
            }
        }
    }
}
