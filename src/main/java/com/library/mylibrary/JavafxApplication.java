package com.library.mylibrary;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class JavafxApplication extends Application {

    final ApplicationContextInitializer<GenericApplicationContext> initializer = applicationContext -> {
        applicationContext.registerBean(Application.class, () -> JavafxApplication.this);
        applicationContext.registerBean(Parameters.class, this::getParameters);
        applicationContext.registerBean(HostServices.class, this::getHostServices);
    };
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder()
                .sources(MyLibraryApplication.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) {
        this.applicationContext.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
}

