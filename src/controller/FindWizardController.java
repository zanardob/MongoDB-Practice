package controller;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
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
import org.bson.BSON;
import org.bson.Document;
import util.MongoConnector;

import java.lang.reflect.Array;
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
    private MongoConnector mongoc;

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

        MongoDatabase db = MongoConnector.getDatabase();
        MongoCollection dbCollection = db.getCollection(collection.getName());

        ArrayList<Document> doc = new ArrayList<>();
        dbCollection.find(BasicDBObject.parse(query)).into(doc);

        txaResults.clear();
        for(int i = 0; i < doc.size(); i++) {
            txaResults.setText(txaResults.getText() + doc.get(i).toJson() + "\n");
        }
    }
}
