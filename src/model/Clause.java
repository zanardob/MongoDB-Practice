package model;

import javafx.scene.layout.HBox;

public abstract interface Clause {
    String getValue();
    void addToHBox(HBox hBox);
}