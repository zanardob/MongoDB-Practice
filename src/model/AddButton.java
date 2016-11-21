package model;

import controller.AddClauseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;
import java.util.ArrayList;

public class AddButton extends Button {
    private HBox hBox;
    private CollectionInfo collection;

    private Clause clause;
    ArrayList<Clause> clauses;
    private boolean list = false;

    public AddButton(HBox hBox, CollectionInfo collection) {
        Effect icon = new ImageInput(new Image("file:art\\add.png"));

        setMinSize(15, 15);
        setPrefSize(15, 15);
        setEffect(icon);

        this.hBox = hBox;
        this.collection = collection;

        setOnAction(event -> add());
    }

    public Clause getClause() {
        return clause;
    }

    public AddButton(HBox hBox, CollectionInfo collection, ArrayList<Clause> clauses) {
        Effect icon = new ImageInput(new Image("file:art\\add.png"));

        setMinSize(15, 15);
        setPrefSize(15, 15);
        setEffect(icon);

        this.clauses = clauses;
        if(clauses != null) {
            list = true;
        }
        this.hBox = hBox;
        this.collection = collection;

        setOnAction(event -> add());
    }

    private void add() {
        Stage addClause = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addclauseview.fxml"));

        try {
            Parent root;
            root = loader.load();

            AddClauseController controller = loader.getController();
            controller.setHBox(hBox);
            controller.setCollection(collection);
            controller.setClause(clause);

            addClause.setScene(new Scene(root));
            addClause.setTitle("Add Clause");
            addClause.initModality(Modality.APPLICATION_MODAL);
            addClause.initOwner(this.getScene().getWindow());
            addClause.showAndWait();

            if(controller.isAdded()) {
                hBox.getChildren().remove(this);
                clause = controller.getClause();
                clause.addToHBox(hBox);
                if(list) {
                    clauses.add(clause);
                    hBox.getChildren().add(new Label(" , "));
                    hBox.getChildren().add(new AddButton(hBox, collection, clauses));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
