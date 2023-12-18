package com.library.mylibrary.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ChangeCenterPaneEvent extends Event {

    public static final EventType<ChangeCenterPaneEvent> CHANGE_CENTER_PANE =
            new EventType<>(Event.ANY, "CHANGE_CENTER_PANE");

    private final String fxmlFilePath;

    public ChangeCenterPaneEvent(Object source, String fxmlFilePath) {
        super(source, null, CHANGE_CENTER_PANE);
        this.fxmlFilePath = fxmlFilePath;
    }

    public String getFXMLFilePath() {
        return fxmlFilePath;
    }
}
