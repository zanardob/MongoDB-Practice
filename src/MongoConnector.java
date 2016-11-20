import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoConnector {
    private static final String databaseName = "test";
    private static MongoDatabase db = null;

    private static MongoDatabase connect(){
        MongoClient client = new MongoClient();
        db = client.getDatabase(databaseName);

        return db;
    }

    public static MongoDatabase getDatabase(){
        if(db != null)
            return db;

        return connect();
    }

}
