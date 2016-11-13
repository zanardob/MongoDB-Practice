import com.mongodb.BasicDBObject;

import java.sql.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] argv){
        createBSON("LE12PESQUISA");
    }

    /*File criaBSON(String tableName) throws SQLException, ClassNotFoundException {
        //Connection connection = DatabaseConnector.getConnection();




        // Connecta no oracu, pega a tabela e cria o baison dela (retorna um .bson)
        return null;
    }

    File criaBSON(String tableName, String tableEmbed){
        // Connecta no oracu, pega a tabela e cria o baison dela (retorna um .bson)
        return null;
    }

    void insereBSON(String mongoConnection, File bsonFile){
        // Connection connection = MongoConnector.getConnection();
        // Pega o arquivo passado e insere no mongo especificado
    }*/

    private static void createBSON(String tableName) {
        String tableDDL = null;
        try {
            tableDDL = DataManager.getTableDDL(tableName);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<ForeignKey> foreignKeys = new ArrayList<>();
        ArrayList<String> primaryKeys = new ArrayList<>();

        // This split gets all the constraints
        String[] constraints = tableDDL.split("CONSTRAINT");
        constraints = Arrays.copyOfRange(constraints, 1, constraints.length);

        for(String constraint : constraints){
            // This split removes the name of the constraint
            String token = constraint.split("^ *\"[^\\\"]*\" ?")[1];

            if(token.startsWith("FOREIGN KEY")){
                ForeignKey fk = new ForeignKey();

                // Splits the token to get the appropriate values needed to access the correct fields on the referenced table
                // Also removes any empty strings that result from the "split" function call
                fk.setMyFields(Arrays.stream(token.split("^.*\\(|\\)[\\S\\s]*|[\", ]"))
                        .filter(str -> !str.isEmpty()).collect(Collectors.toList()).toArray(new String[0]));

                fk.setForeignFields(Arrays.stream(token.split("^.*\\([\\s\\S]*?\\(|\\)[\\s\\S]*|[\", ]"))
                        .filter(str -> !str.isEmpty()).collect(Collectors.toList()).toArray(new String[0]));

                fk.setForeignTable(token.split("^[\\s\\S]*\\.\"|\" [\\S\\s]*")[0]);

                // Add this foreign key to the list of foreign keys
                foreignKeys.add(fk);

            } else if(token.startsWith("PRIMARY KEY")){
                // Get the fields that are used as the primary key
                // Also removes any empty strings that result from the "split" function call
                primaryKeys = new ArrayList<>(Arrays.stream(token.split("^.*\\(|\\)[\\S\\s]*|[\", ]"))
                        .filter(str -> !str.isEmpty()).collect(Collectors.toList()));
            }
        }

        try {
            ResultSet rs = DataManager.getData(tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Builds an ArrayList that contains all the columnNames to be written on the BSON
            ArrayList<String> columnNames = new ArrayList<>();
            ArrayList<Integer> columnTypes = new ArrayList<>();
            for(int i = 1; i <= columnCount; i++) {
                columnNames.add(rsmd.getColumnName(i));
                columnTypes.add(rsmd.getColumnType(i));

                // Prints the values for the columnTypes -- DEBUG
                System.out.println("Type of column " + columnNames.get(i-1) + ": " + columnTypes.get(i-1));
            }

            // Iterates over every tuple of the table
            ArrayList<BasicDBObject> collection = new ArrayList<>();
            while(rs.next()) {
                BasicDBObject currentObject;
                BasicDBObject document = new BasicDBObject();
                BasicDBObject primaryKey = new BasicDBObject();

                for(int i = 0; i < columnCount; i++) {
                    String currentColumn = columnNames.get(i);

                    if(primaryKeys.contains(currentColumn)) {
                        // This column is part of the primary key; add it to primaryKey object
                        currentObject = primaryKey;
                    } else {
                        // This is not part of the primary key; add it as another field on the document
                        currentObject = document;
                    }

                    Object value;
                    switch(columnTypes.get(i)){
                        case Types.NUMERIC:
                            value = rs.getInt(i+1);

                            if (!rs.wasNull())
                                currentObject.put(currentColumn, value);
                            break;

                        case Types.CHAR:
                        case Types.VARCHAR:
                            value = rs.getString(i+1);

                            if (!rs.wasNull())
                                currentObject.put(currentColumn, value);
                            break;

                        case Types.TIMESTAMP:
                            value = rs.getTimestamp(i+1);

                            if (!rs.wasNull()) {
                                long timestamp = ((Timestamp) value).getTime();
                                currentObject.put(currentColumn, new Date(timestamp));
                            }
                            break;
                    }
                }

                // Adds the primary key to the document
                document.put("_id", primaryKey);

                // Adds the document to the collection
                collection.add(document);
            }

            // Prints the resulting collection with all the documents -- DEBUG
            for(BasicDBObject document : collection)
                System.out.println(document.toString());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
