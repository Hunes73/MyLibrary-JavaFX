package com.library.mylibrary.controller;

import com.library.mylibrary.data.entity.Author;
import com.library.mylibrary.data.enums.Role;
import com.library.mylibrary.model.BookModel;
import com.library.mylibrary.service.impl.BookServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class BookController implements Initializable {

    private final MainController mainController;

    private final BookServiceImpl bookService;

    private final BookDetailsController bookDetailsController;

    @FXML
    public Button addButton, editButton, deleteButton;

    @FXML
    public TableView<BookModel> tableView;

    @FXML
    public TableColumn<BookModel, String> titleColumn, authorColumn, genreColumn, descriptionColumn;

    public BookController(MainController mainController, BookServiceImpl bookService, BookDetailsController bookDetailsController) {
        this.mainController = mainController;
        this.bookService = bookService;
        this.bookDetailsController = bookDetailsController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (mainController.getLoggedUser().getRole().equals(Role.READER)) {
            addButton.setVisible(false);
            editButton.setVisible(false);
            deleteButton.setVisible(false);
        }
        tableView.setItems(bookService.getAllBooks());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitle());
        authorColumn.setCellValueFactory(cellData -> {
            List<Author> authors = cellData.getValue().getAuthors();
            return new SimpleStringProperty(authors.stream().map(author -> author.getFirstname() + " " + author.getLastname()).collect(Collectors.joining(", ")));
        });
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().getGenre());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescription());
    }

    public void addBook() throws IOException {
        bookDetailsController.setMode("create");
        mainController.switchCenterPane("/fxml/book_details.fxml");
    }

    public void deleteBook() throws IOException {
        BookModel bookModel = tableView.getSelectionModel().getSelectedItem();
        if (bookModel == null) {
            mainController.showPopup("Nie wybrano książki");
            return;
        }
        bookService.deleteBook(bookModel.getId());
        tableView.setItems(bookService.getAllBooks());
    }

    public void editBook() throws IOException {
        BookModel bookModel = tableView.getSelectionModel().getSelectedItem();
        if (bookModel == null) {
            mainController.showPopup("Nie wybrano książki");
            return;
        }
        bookDetailsController.setMode("update");
        bookDetailsController.setBookModel(bookModel);
        mainController.switchCenterPane("/fxml/book_details.fxml");
    }
}
