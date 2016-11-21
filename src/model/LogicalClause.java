package model;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Objects;

public class LogicalClause implements Clause{
    private ArrayList<Clause> clauses;
    private String operationCommand;
    private Label operation;
    private Label closeParenthesis;
    private HBox hBox;
    private HBox innerHBox;

    /**
     * This is the constructor for the LogicalClause,
     * containing all the information necessary to build a LogicalClause
     */
    public LogicalClause(String operationCommand, CollectionInfo collection) {
        clauses = new ArrayList<>();
        hBox = new HBox();
        innerHBox = new HBox();
        this.operation = new Label();
        switch (operationCommand) {
            case "AND":
                this.operationCommand = "$and";
                break;
            case "OR":
                this.operationCommand = "$or";
                break;
            case "NOR":
                this.operationCommand = "$nor";
                break;
        }
        this.operation.setText(operationCommand + "( ");
        this.closeParenthesis = new Label(" )");
        innerHBox.getChildren().add(new AddButton(innerHBox, collection, clauses));
        hBox.getChildren().addAll(operation, innerHBox, closeParenthesis);
    }

    @Override
    public String getValue() {
        String value;
        int size = clauses.size();
        if(size > 0) {
            value = "{ " + operationCommand + ": [ ";
            for(int i = 0; i < size; i++) {
                if(i < size - 1) {
                    value = value + clauses.get(i).getValue() + ", ";
                }
                else {
                    value = value + clauses.get(i).getValue() + " ] }";
                }
            }
        }
        else {
            value = "{}";
        }

        return value;
    }


    @Override
    public void addToHBox(HBox hBox) {
        hBox.getChildren().add(this.hBox);
    }
}
