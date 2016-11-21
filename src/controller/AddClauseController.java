package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddClauseController implements Initializable {
    private HBox hBox;
    private CollectionInfo collection;
    private boolean added;
    private Clause clause;

    BorderPane bp = new BorderPane();
    Label label = new Label();
    ComboBox<String> cBox = new ComboBox<>();
    ObservableList<String> items = FXCollections.observableArrayList();

    BorderPane bpField = new BorderPane();
    Label fieldLabel = new Label();
    ComboBox<String> cboxField = new ComboBox<>();
    ObservableList<String> fields = FXCollections.observableArrayList();

    BorderPane bpComparison = new BorderPane();
    Label comparisonLabel = new Label();
    TextField comparisonValue = new TextField();
    RadioButton negationButton = new RadioButton();

    @FXML public VBox vboxFields;
    @FXML public ComboBox cboxType;

    /**
     * This function sets the initial parameters needed when the window opens
     * (The only things that we know for sure are the possible classes (Comparison, Logical or Type)
     * The remaining values are loaded dynamically depending on the user's class choice.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        added = false;
        ObservableList<String> types = FXCollections.observableArrayList();
        types.addAll("Comparison", "Logical", "Type");
        cboxType.setItems(types);
    }

    public boolean isAdded() {
        return added;
    }

    public void closeWindow() {
        Stage stage = (Stage) vboxFields.getScene().getWindow();
        stage.close();
    }

    /**
     * Gathers the information input by the user and creates a clause cointaining it.
     * If it cannot create a clause (incomplete information), it will do nothing.
     */
    public void confirm(ActionEvent actionEvent) {
        if(!Objects.equals(cboxType.getValue(), null)) {
            AttributeInfo attribute = new AttributeInfo();
            switch (cboxType.getValue().toString()) {
                case "Comparison":
                    if(!Objects.equals(cBox.getValue(), null) && !Objects.equals(cboxField.getValue(), null) && !Objects.equals(comparisonValue.getText(), "")) {
                        for(AttributeInfo att : collection.getAttributes()) {
                            if(Objects.equals(att.getName(), cboxField.getValue())) {
                                attribute = att;
                                break;
                            }
                        }
                        clause = new ComparisonClause(attribute, cBox.getValue(), comparisonValue.getText(), negationButton.isSelected());
                        added = true;
                        closeWindow();
                    }
                    break;
                case "Logical":
                    if(!Objects.equals(cBox.getValue(), null)) {
                        clause = new LogicalClause(cBox.getValue(), collection);
                        added = true;
                        closeWindow();
                    }
                        break;
                case "Type":
                    if(!Objects.equals(cBox.getValue(), null) &&!Objects.equals(cboxField.getValue(), null) ) {
                        for(AttributeInfo att : collection.getAttributes()) {
                            if(Objects.equals(att.getName(), cboxField.getValue())) {
                                attribute = att;
                                break;
                            }
                        }
                        clause = new TypeClause(attribute, cBox.getValue());
                        added = true;
                        closeWindow();
                    }
                    break;
            }

        }
    }

    public void setHBox(HBox hBox) {
        this.hBox = hBox;
    }

    public void setCollection(CollectionInfo collection) {
        this.collection = collection;
    }

    /**
     * This function is triggered by the click on the ComboBox (Comparison, Value or Type)
     * It will display the rest of the options to the user accordingly to his or her previous ComboBox choice.
     */
    public void chooseType(ActionEvent actionEvent) {
        fieldLabel.setText("Field");
        fieldLabel.setFont(Font.font(14));

        vboxFields.getChildren().clear();
        switch (cboxType.getValue().toString()) {
            case "Comparison":
                Label negationLabel = new Label();
                BorderPane bpNegation = new BorderPane();
                negationLabel.setText("Negate?");
                bpNegation.setLeft(negationLabel);
                bpNegation.setRight(negationButton);

                comparisonLabel.setText("Value");
                comparisonLabel.setFont(Font.font(14));
                bpComparison.setLeft(comparisonLabel);
                bpComparison.setRight(comparisonValue);

                for(AttributeInfo att : collection.getAttributes()) {
                    fields.add(att.getName());
                }
                cboxField.setItems(fields);
                bpField.setLeft(fieldLabel);
                bpField.setRight(cboxField);

                label.setText("Operation: ");
                label.setFont(Font.font(14));
                bp.setLeft(label);
                items.clear();
                items.addAll("Equal to", "Greater than", "Greater/equal to", "Less than", "Less/equal to", "Not equal to", "In list", "Not in list");
                cBox.setItems(items);
                bp.setRight(cBox);
                vboxFields.getChildren().addAll(bpField, bp, bpComparison, bpNegation);
                break;
            case "Logical":
                label.setText("Operation: ");
                label.setFont(Font.font(14));
                bp.setLeft(label);
                items.clear();
                items.addAll("OR", "AND", "NOR");
                cBox.setItems(items);
                bp.setRight(cBox);
                vboxFields.getChildren().add(bp);
                break;
            case "Type":
                for(AttributeInfo att : collection.getAttributes()) {
                    fields.add(att.getName());
                }
                cboxField.setItems(fields);
                bpField.setLeft(fieldLabel);
                bpField.setRight(cboxField);

                label.setText("Type: ");
                label.setFont(Font.font(14));
                bp.setLeft(label);
                items.clear();
                items.addAll("String", "Date", "Number");
                cBox.setItems(items);
                bp.setRight(cBox);
                vboxFields.getChildren().addAll(bpField, bp);
                break;
        }
    }

    public void setClause(Clause clause) {
        this.clause = clause;
    }

    public Clause getClause() {
        return clause;
    }
}
