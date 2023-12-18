package com.library.mylibrary;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
@Log4j2
public class StageListener implements ApplicationListener<StageReadyEvent> {

    private final String applicationTitle;
    private final Resource fxml;
    private final Image icon;
    private final ApplicationContext applicationContext;

    StageListener(@Value("${spring.application.ui.title}") String applicationTitle,
                  @Value("classpath:/fxml/login.fxml") Resource fxml,
                  @Value("classpath:/icons/logo.png") Resource iconResource,
                  ApplicationContext applicationContext) throws IOException {
        this.applicationTitle = applicationTitle;
        this.fxml = fxml;
        this.icon = new Image(iconResource.getURL().toExternalForm());
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(@NonNull StageReadyEvent event) {
        try {
            Stage stage = event.getStage();
            URL url = this.fxml.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 854, 480);
            stage.setResizable(false);
            stage.setTitle(this.applicationTitle);
            stage.setScene(scene);
            stage.getIcons().add(this.icon);
            stage.show();
        } catch (Exception ex) {
            log.error("Błąd w czasie wczytywania sceny", ex);
        }
    }
}