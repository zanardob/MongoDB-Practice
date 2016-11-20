import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PerformanceManager {
    private final Random rng = new Random(System.nanoTime());
    private final String candidates = "ABCDEFGHJIKLMNOPQRSTUVWXYZ0123456789";
    private final int MAX_SIZE = 128;
    private String tableName;

    public PerformanceManager(String tableName) {
        this.tableName = tableName;
    }

    private String randomString(Random rng, String candidateChars, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(candidateChars.charAt(rng.nextInt(candidateChars.length())));

        return sb.toString();
    }

    private Document generateDocument(){
        Document document = new Document();
        document
                .append("nome1",  randomString(rng, candidates, 2000))
                .append("nome2",  randomString(rng, candidates, 2000))
                .append("nome3",  randomString(rng, candidates, 2000))
                .append("nome4",  randomString(rng, candidates, 2000))
                .append("nome5",  randomString(rng, candidates, 2000))
                .append("nome6",  randomString(rng, candidates, 2000))
                .append("nome7",  randomString(rng, candidates, 2000))
                .append("nome8",  randomString(rng, candidates, 2000))
                .append("nome9",  randomString(rng, candidates, 2000))
                .append("nome10", randomString(rng, candidates, 2000));

        return document;
    }

    public void insertionTest(int count){
        MongoDatabase db = MongoConnector.getDatabase();
        MongoCollection<Document> collection = db.getCollection(tableName);

        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++)
            collection.insertOne(generateDocument());
        long endTime = System.nanoTime();

        System.out.printf("%d insertions on table %s made in %.2f seconds!\n", count, tableName, (endTime - startTime) / 1000000000.0);
    }

    public void findTest(int count){
        MongoDatabase db = MongoConnector.getDatabase();
        MongoCollection<Document> collection = db.getCollection(tableName);

        BasicDBObject aggregationParameter = new BasicDBObject("$sample", new BasicDBObject("size", MAX_SIZE));
        ArrayList<Document> findCache = collection.aggregate(Collections.singletonList(aggregationParameter)).into(new ArrayList<>());

        String nome1;
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            nome1 = findCache.get(rng.nextInt(MAX_SIZE)).getString("nome1");
            BasicDBObject findParameter = new BasicDBObject("nome1", nome1);

            collection.find(findParameter);
        }
        long endTime = System.nanoTime();

        System.out.printf("%d finds on table %s made in %.2f seconds!\n", count, tableName, (endTime - startTime) / 1000000000.0);
    }
}
