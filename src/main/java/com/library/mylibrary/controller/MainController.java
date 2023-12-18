package com.library.mylibrary.controller;

import com.library.mylibrary.data.entity.User;
import com.library.mylibrary.data.enums.Role;
import com.library.mylibrary.event.ChangeCenterPaneEvent;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Log4j2
public class MainController implements Initializable {

    private final ApplicationContext applicationContext;

    @FXML
    private BorderPane borderPane;

    @Getter
    @Setter
    private User loggedUser;

    @FXML
    public VBox sideBox;

    @FXML
    private Button loansButton, borrowersButton, myBooksButton;

    MainController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void openLoans() throws IOException {
        switchCenterPane("/fxml/loans.fxml");
    }

    public void openBorrowers() throws IOException {
        switchCenterPane("/fxml/borrowers.fxml");
    }

    public void openCopies() throws IOException {
        switchCenterPane("/fxml/copies.fxml");
    }

    public void openBooks() throws IOException {
        switchCenterPane("/fxml/books.fxml");
    }

    public void openAuthors() throws IOException {
        switchCenterPane("/fxml/authors.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Node> children = sideBox.getChildren();
        if (loggedUser == null) {
            loggedUser = new User();
            loggedUser.setRole(Role.ADMIN);
        }
        if (loggedUser.getRole().equals(Role.READER)) {
            children.remove(loansButton);
            children.remove(borrowersButton);
        } else {
            children.remove(myBooksButton);
        }
        try {
            switchCenterPane("/fxml/authors.fxml");
        } catch (IOException ex) {
            log.error("Błąd w czasie inicjalizacji głównej sceny", ex);
        }

        EventHandler<ChangeCenterPaneEvent> changeCenterPaneEventHandler = event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(event.getFXMLFilePath()));
            loader.setControllerFactory(applicationContext::getBean);
            try {
                borderPane.setCenter(loader.load());
            } catch (IOException ex) {
                log.error("Błąd w czasie zmiany głównej sceny", ex);
            }
        };

        borderPane.addEventHandler(ChangeCenterPaneEvent.CHANGE_CENTER_PANE, changeCenterPaneEventHandler);
    }

    public void switchCenterPane(String fxmlFilePath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        loader.setControllerFactory(applicationContext::getBean);
        Parent newPane = loader.load();
        borderPane.setCenter(newPane);
    }

    public void showPopup(String message) throws IOException {
        PopupController popupController = this.applicationContext.getBean(PopupController.class);
        popupController.setMessage(message);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popup.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        popupStage.setResizable(false);
        popupStage.setScene(new Scene(root));
        popupStage.showAndWait();
    }
}