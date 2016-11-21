package model;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.Objects;

public class ComparisonClause implements Clause{
    private AttributeInfo attribute;
    private String operationCommand;
    private HBox hBox;
    private Label fieldName;
    private Label operation;
    private Label value;
    private boolean negation;

    /**
     * This is the constructor for the ComparisonClause,
     * containing all the information necessary to build a ComparisonClause
     */
    public ComparisonClause(AttributeInfo attribute, String operation, String value, boolean negation) {
        hBox = new HBox();
        this.negation = negation;
        this.attribute = attribute;
        this.fieldName = new Label();
        this.operation = new Label();
        this.value = new Label();
        this.fieldName.setFont(Font.font(14));
        this.operation.setFont(Font.font(14));
        this.value.setFont(Font.font(14));
        if(negation) {
            this.fieldName.setText("NOT " + attribute.getName() + " ");
        }
        else {
            this.fieldName.setText(attribute.getName() + " ");
        }
        this.value.setText(value);
        switch (operation) {
            case "Equal to":
                this.operation.setText("=");
                this.operationCommand = "$eq";
                break;
            case "Greater then":
                this.operation.setText(">");
                this.operationCommand = "$gt";
                break;
            case "Greater/equal to":
                this.operation.setText(">=");
                this.operationCommand = "$gte";
                break;
            case "Less then":
                this.operation.setText("<");
                this.operationCommand = "$lt";
                break;
            case "Less/equal to":
                this.operation.setText("<=");
                this.operationCommand = "$lte";
                break;
            case "Not equal to":
                this.operation.setText("!=");
                this.operationCommand = "$ne";
                break;
            case "In list":
                this.operation.setText("IN");
                this.operationCommand = "$in";
                this.value.setText("[ " + value + " ]");
                break;
            case "Not in list":
                this.operation.setText("NOT IN");
                this.operationCommand = "$nin";
                this.value.setText("[ " + value + " ]");
                break;
        }
        hBox.getChildren().addAll(this.fieldName, this.operation, this.value);
    }

    @Override
    public String getValue() {
        String value;
        if(attribute.isEmbedded()) {
            value = "{ \"" + attribute.getRealName() + "\"";
        }
        else {
            value = "{ " + attribute.getName();
        }
        if(negation) {
            value = value + ": { $not: {" + operationCommand + ": " + this.value.getText() + " } } }";
        }
        else {
            value = value + ": { " + operationCommand + ": " + this.value.getText() + " } }";
        }

        return value;
    }

    @Override
    public void addToHBox(HBox hBox) {
        hBox.getChildren().add(this.hBox);
    }
}
