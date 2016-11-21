package model;

import javafx.scene.layout.HBox;

public interface Clause {
    String getValue();
    void addToHBox(HBox hBox);
}