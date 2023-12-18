package com.library.mylibrary.controller;

import com.library.mylibrary.model.AuthorModel;
import com.library.mylibrary.model.BookModel;
import com.library.mylibrary.service.impl.AuthorServiceImpl;
import com.library.mylibrary.service.impl.BookServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class BookDetailsController implements Initializable {

    private final MainController mainController;

    private final BookServiceImpl bookService;

    private final AuthorServiceImpl authorService;
    @FXML
    public ListView<AuthorModel> availableListView, selectedListView;
    @Setter
    private String mode;
    @Setter
    private BookModel bookModel;
    @FXML
    private Label title;
    @FXML
    private TextField titleField, genreField, filterField;
    @FXML
    private TextArea descriptionField;
    private ObservableList<AuthorModel> allAuthors, selectedAuthors, availableAuthors;

    public BookDetailsController(MainController mainController, BookServiceImpl bookService, AuthorServiceImpl authorService) {
        this.mainController = mainController;
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allAuthors = FXCollections.observableArrayList(authorService.getAllAuthors());
        switch (mode) {
            case "update" -> {
                title.setText("Edytuj kasążkę");
                titleField.setText(bookModel.getTitle().getValue());
                genreField.setText(bookModel.getGenre().getValue());
                descriptionField.setText(bookModel.getDescription().getValue());
                selectedAuthors = (bookModel.getAuthors().stream().map(authorService::mapToModel).collect(Collectors.toCollection(FXCollections::observableArrayList)));
                List<Long> selectedAuthorsIds = selectedAuthors.stream().map(AuthorModel::getId).toList();
                availableAuthors = allAuthors.stream()
                        .filter(authorModel -> !selectedAuthorsIds.contains(authorModel.getId()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            case "create" -> {
                title.setText("Utwórz książkę");
                selectedAuthors = FXCollections.observableArrayList();
                availableAuthors = FXCollections.observableArrayList();
                availableAuthors.setAll(allAuthors);
            }
        }

        availableListView.setItems(availableAuthors);
        selectedListView.setItems(selectedAuthors);

        setAuthorCellFactory(availableListView);
        setAuthorCellFactory(selectedListView);


        filterField.textProperty().addListener((observable, oldValue, newValue) -> filterAuthors(newValue));

        availableListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                addAuthor();
            }
        });

        selectedListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                removeAuthor();
            }
        });
    }

    private void filterAuthors(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            availableAuthors.setAll(allAuthors);
        } else {
            List<AuthorModel> filteredList = allAuthors.stream()
                    .filter(authorModel -> authorModel.getFirstname().getValue().toLowerCase().contains(filterText.toLowerCase())
                            || authorModel.getLastname().getValue().toLowerCase().contains(filterText.toLowerCase()))
                    .collect(Collectors.toList());
            availableAuthors.setAll(filteredList);
        }
    }

    private void addAuthor() {
        AuthorModel selectedAuthor = availableListView.getSelectionModel().getSelectedItem();
        if (selectedAuthor != null) {
            availableAuthors.remove(selectedAuthor);
            selectedAuthors.add(selectedAuthor);
        }
    }

    private void removeAuthor() {
        AuthorModel selectedAuthor = selectedListView.getSelectionModel().getSelectedItem();
        if (selectedAuthor != null) {
            selectedAuthors.remove(selectedAuthor);
            availableAuthors.add(selectedAuthor);
        }
    }

    public void save() throws IOException {
        if (titleField.getText().isEmpty()) {
            mainController.showPopup("Pole tytuł musi być wypełnione!");
            return;
        }

        if (bookModel == null) {
            bookModel = new BookModel();
        }
        bookModel.setTitle(new SimpleStringProperty(titleField.getText()));
        bookModel.setGenre(new SimpleStringProperty(genreField.getText()));
        bookModel.setDescription(new SimpleStringProperty(descriptionField.getText()));
        bookModel.setAuthors(selectedAuthors.stream().map(authorModel -> authorService.getAuthorById(authorModel.getId())).collect(Collectors.toList()));

        switch (mode) {
            case "update" -> bookService.updateBook(bookModel);
            case "create" -> bookService.createBook(bookModel);
        }
        mainController.switchCenterPane("/fxml/books.fxml");
    }

    public void cancel() throws IOException {
        mainController.switchCenterPane("/fxml/books.fxml");
    }

    private void setAuthorCellFactory(ListView<AuthorModel> listView) {
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(AuthorModel author, boolean empty) {
                super.updateItem(author, empty);
                if (empty || author == null) {
                    setText(null);
                } else {
                    setText(author.getFirstname().getValue() + " " + author.getLastname().getValue());
                }
            }
        });
    }
}
