package controller;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
import org.bson.Document;
import util.MongoConnector;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FindWizardController implements Initializable {
    @FXML public ComboBox cboxCollectionSelect;
    @FXML public HBox hboxQueryBuilder;
    @FXML public TextField txtLastExecQuery;
    @FXML public TextArea txaResults;

    private DatabaseInfo dbinfo;
    private CollectionInfo collection;
    private Clause clause;
    private AddButton primeButton;

    /**
     * This function sets the initial parameters to initialize the wizard,
     * like collecting the DatabaseData from the DatabaseInfo class.
     * The information is specific for THIS database, and will require a new DatabaseInfo class
     * for other Databases.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbinfo = new DatabaseInfo();
        ObservableList<String> collectionNames = FXCollections.observableArrayList();
        collectionNames.addAll(dbinfo.getCollectionNames());
        cboxCollectionSelect.setItems(collectionNames);
    }

    /**
     * Selects the correct collection from the DB, so the user can perform queries on.
     * This also creates the button that will be the query (outer) clause.
     */
    public void chooseCollection() {
        collection = dbinfo.getCollection(cboxCollectionSelect.getValue().toString());

        hboxQueryBuilder.getChildren().clear();
        primeButton = new AddButton(hboxQueryBuilder, collection);
        hboxQueryBuilder.getChildren().add(primeButton);
    }

    /**
     * Once a query is built, hitting the Execute Query button will call this method.
     * It will get the query clause from the prime button and will connect to the MongoDB connector,
     * performing the query and displaying the result JSON on the Results Text Area.
     */
    public void executeQuery(ActionEvent actionEvent) {
        if(collection == null) {
            return;
        }
        String query;
        clause = primeButton.getClause();
        if(clause != null) {
            query = clause.getValue();
        }
        else {
            query = "{ }";
        }
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

    public void clearQuery(ActionEvent actionEvent) {
        if(collection != null) {
            chooseCollection();
        }
    }
}
