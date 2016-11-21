package model;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class TypeClause implements Clause {
    private AttributeInfo attribute;
    private String literalType;
    private HBox hBox;
    private Label fieldName;
    private Label type;

    public TypeClause(AttributeInfo attribute, String type) {
        hBox = new HBox();
        this.attribute = attribute;
        this.fieldName = new Label();
        this.type = new Label();
        this.fieldName.setFont(Font.font(14));
        this.type.setFont(Font.font(14));
        this.fieldName.setText(attribute.getName() + " ");
        this.type.setText("IS " + type);
        switch (type) {
            case "String":
                literalType = "\"string\"";
                break;
            case "Number":
                literalType = "\"number\"";
                break;
            case "Date":
                literalType = "\"date\"";
                break;
        }
        hBox.getChildren().addAll(this.fieldName, this.type);
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
        value = value + ": { $type: " + literalType + " } }";
        return value;
    }

    @Override
    public void addToHBox(HBox hBox) {
        hBox.getChildren().add(this.hBox);
    }
}
