import com.mongodb.BasicDBObject;

import java.util.ArrayList;

public class Main {
    public static void main(String[] argv){
        MongoConverter mc = new MongoConverter();
        ArrayList<BasicDBObject> collection = mc.buildCollection("LE08CANDIDATO", "PARTIDO");

        for(BasicDBObject bdbo : collection)
            System.out.println(bdbo.toString());
    }

    /*
    void insereBSON(String mongoConnection, File bsonFile){
        // Connection connection = MongoConnector.getConnection();
        // Pega o arquivo passado e insere no mongo especificado
    }
    */
}
