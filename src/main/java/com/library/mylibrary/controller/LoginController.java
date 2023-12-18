package com.library.mylibrary.controller;

import com.library.mylibrary.service.impl.UserServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginController {

    private final ApplicationContext applicationContext;

    private final MainController mainController;

    private final UserServiceImpl userService;

    @FXML
    public TextField cardNumberField;

    @FXML
    PasswordField passwordField;

    @FXML
    private Button loginButton;

    public LoginController(ApplicationContext applicationContext, UserServiceImpl userService, MainController mainController) {
        this.applicationContext = applicationContext;
        this.userService = userService;
        this.mainController = mainController;
    }

    @FXML
    protected void login() throws IOException {
//            MainController mainController = applicationContext.getBean(MainController.class);
        if (cardNumberField.getText().equals("admin") && passwordField.getText().equals("admin")) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            loginButton.getScene().setRoot(fxmlLoader.load());
        } else if (userService.getAllCardNumbers().stream().anyMatch(cardNumberField.getText()::equals)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(passwordField.getText(), userService.getPasswordByCardNumber(cardNumberField.getText()))) {
                mainController.setLoggedUser(userService.getUserByCardNumber(cardNumberField.getText()));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                loginButton.getScene().setRoot(fxmlLoader.load());
            } else {
                mainController.showPopup("Niepoprawne hasło do logowania");
            }
        } else {
            mainController.showPopup("Niepoprawne dane logowania");
        }

    }
}