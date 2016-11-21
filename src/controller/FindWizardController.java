package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FindWizardController implements Initializable {
    @FXML public ComboBox cboxCollectionSelect;
    @FXML public HBox hboxQueryBuilder;
    @FXML public TextField txtLastExecQuery;
    @FXML public TextArea txaResults;

    private DatabaseInfo dbinfo;
    private ArrayList<AttributeInfo> attributes;
    private CollectionInfo collection;
    private Clause clause;
    private AddButton primeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbinfo = new DatabaseInfo();
        ObservableList<String> collectionNames = FXCollections.observableArrayList();
        collectionNames.addAll(dbinfo.getCollectionNames());
        cboxCollectionSelect.setItems(collectionNames);
    }

    public void getCollectionAttributes(ActionEvent actionEvent) {
        collection = dbinfo.getCollection(cboxCollectionSelect.getValue().toString());
        attributes = collection.getAttributes();
        String s = "";
        for(AttributeInfo att : attributes) {
            s = s + ", " + att.getName();
        }
        txaResults.setText(s);

        hboxQueryBuilder.getChildren().clear();
        primeButton = new AddButton(hboxQueryBuilder, collection);
        hboxQueryBuilder.getChildren().add(primeButton);
    }

    public void executeQuery(ActionEvent actionEvent) {
        String query;
        clause = primeButton.getClause();
        query = clause.getValue();
        txtLastExecQuery.setText(query);
        System.out.println(query);

        // TODO PEGAR OS LANCE DO MONGO E PREENCHER O TEXTAREA
    }
}
