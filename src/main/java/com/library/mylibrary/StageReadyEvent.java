package com.library.mylibrary;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

class StageReadyEvent extends ApplicationEvent {
    public StageReadyEvent(Stage stage) {
        super(stage);
    }

    public Stage getStage() {
        return (this.getSource() == null) ? null : (Stage) this.getSource();
    }
}
