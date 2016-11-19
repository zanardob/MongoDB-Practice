import com.google.common.collect.Multimap;
import com.mongodb.BasicDBObject;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class MongoConverter {
    private final String databaseName;

    public MongoConverter(String databaseName) {
        this.databaseName = databaseName;
    }

    public ArrayList<String> buildCollection(String tableName) {
        ArrayList<String> commandList = null;

        try {
            commandList = new ArrayList<>();
            ArrayList<String> primaryKeys = getPrimaryFields(tableName);

            ResultSet rs = DataManager.getData(tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Builds an ArrayList that contains all the columnNames to be written on the BSON
            ArrayList<ColumnMetadata> tableColumns = new ArrayList<>();
            for(int i = 1; i <= columnCount; i++)
                tableColumns.add(new ColumnMetadata(rsmd.getColumnName(i), rsmd.getColumnType(i)));

            // Iterates over every tuple of the table
            ArrayList<BasicDBObject> collection = new ArrayList<>();
            while(rs.next()) {
                BasicDBObject document = buildDocument(rs, primaryKeys, tableColumns);

                // Adds the document to the collection
                collection.add(document);
            }

            // Add the commands to the result list
            commandList.add(databaseName + ".createCollection(\"" + tableName + "\")");

            JsonWriterSettings jws = new JsonWriterSettings(JsonMode.SHELL);
            for (BasicDBObject document : collection)
                commandList.add(databaseName + "." + tableName + ".insert(" + document.toJson(jws) + ")");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return commandList;
    }

    public ArrayList<String> buildCollection(String tableName, String embedName) {
        ArrayList<String> commandList = null;

        try {
            commandList = new ArrayList<>();
            String tableDDL = DataManager.getTableDDL(tableName);

            ArrayList<String> primaryKeys = new ArrayList<>();
            ForeignKey embedKey = new ForeignKey();

            // This split gets all the constraints
            String[] constraints = tableDDL.split("CONSTRAINT");
            constraints = Arrays.copyOfRange(constraints, 1, constraints.length);

            boolean foundForeign = false;
            for(String constraint : constraints){
                // This split removes the name of the constraint
                String token = constraint.split("^ *\"[^\\\"]*\" ?")[1];

                if(!foundForeign && token.startsWith("FOREIGN KEY")){
                    String foreignTable = token.split("^[\\s\\S]*\\.\"|\" [\\S\\s]*")[1];

                    if(foreignTable.contains(embedName)) {
                        foundForeign = true;

                        // Splits the token to get the appropriate values needed to access the correct fields on the referenced table
                        // Also removes any empty strings that result from the "split" function call
                        embedKey.setMyFields(new ArrayList<>(Arrays.stream(token.split("^.*\\(|\\)[\\S\\s]*|[\", ]"))
                                .filter(str -> !str.isEmpty()).collect(Collectors.toList())));

                        embedKey.setForeignFields(new ArrayList<>(Arrays.stream(token.split("^.*\\([\\s\\S]*?\\(|\\)[\\s\\S]*|[\", ]"))
                                .filter(str -> !str.isEmpty()).collect(Collectors.toList())));

                        embedKey.setForeignTable(foreignTable);
                    }
                } else if(token.startsWith("PRIMARY KEY")){
                    // Get the fields that are used as the primary key
                    // Also removes any empty strings that result from the "split" function call
                    primaryKeys = new ArrayList<>(Arrays.stream(token.split("^.*\\(|\\)[\\S\\s]*|[\", ]"))
                            .filter(str -> !str.isEmpty()).collect(Collectors.toList()));
                }
            }

            ResultSet rs = DataManager.getData(embedKey.getForeignTable());
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Builds an ArrayList that contains all the columnNames to be written on the BSON
            ArrayList<ColumnMetadata> embedColumns = new ArrayList<>();
            for(int i = 1; i <= columnCount; i++)
                embedColumns.add(new ColumnMetadata(rsmd.getColumnName(i), rsmd.getColumnType(i)));

            rs = DataManager.getData(tableName);
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();

            // Builds an ArrayList that contains all the columnNames to be written on the BSON
            ArrayList<ColumnMetadata> tableColumns = new ArrayList<>();
            for(int i = 1; i <= columnCount; i++)
                tableColumns.add(new ColumnMetadata(rsmd.getColumnName(i), rsmd.getColumnType(i)));

            // Removes the fields that are part of the foreign relation that is going to be embedded
            for(String field : embedKey.getMyFields()){
                if(primaryKeys.contains(field)){
                    primaryKeys.remove(field);
                    embedKey.composesPrimary = true;
                }

                // Prevents the fields that compose the foreign key to be added to the document
                tableColumns.stream().filter(c -> Objects.equals(c.getColumnName(), field)).forEach(c -> c.active = false);
            }

            // Iterates over every tuple of the table
            ArrayList<BasicDBObject> collection = new ArrayList<>();
            while(rs.next()) {
                BasicDBObject document = buildDocument(rs, primaryKeys, tableColumns);

                setValues(rs, embedKey, columnCount, tableColumns);

                ResultSet rsEmbed = DataManager.getForeignTuple(embedKey);
                if(rsEmbed.next()) {
                    BasicDBObject embeddedTuple = buildDocument(rsEmbed, embedColumns);

                    if(embedKey.composesPrimary)
                        ((BasicDBObject) document.get("_id")).append(embedName, embeddedTuple);
                    else
                        document.put(embedName, embeddedTuple);
                }

                // Adds the document to the collection
                collection.add(document);
            }

            // Add the commands to the result list
            commandList.add(databaseName + ".createCollection(\"" + tableName + "\")");

            JsonWriterSettings jws = new JsonWriterSettings(JsonMode.SHELL);
            for (BasicDBObject document : collection)
                commandList.add(databaseName + "." + tableName + ".insert(" + document.toJson(jws) + ")");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return commandList;
    }

    public ArrayList<String> buildManyToManyRelationships(String tableName){
        ArrayList<String> commandList = null;

        try {
            commandList = new ArrayList<>();
            String tableDDL = DataManager.getTableDDL(tableName);

            ArrayList<ForeignKey> foreignKeys = new ArrayList<>();

            // This split gets all the constraints
            String[] constraints = tableDDL.split("CONSTRAINT");
            constraints = Arrays.copyOfRange(constraints, 1, constraints.length);

            for(String constraint : constraints){
                // This split removes the name of the constraint
                String token = constraint.split("^ *\"[^\\\"]*\" ?")[1];

                if(token.startsWith("FOREIGN KEY")){
                    String foreignTable = token.split("^[\\s\\S]*\\.\"|\" [\\S\\s]*")[1];
                    foreignTable = foreignTable.substring(4);

                    ForeignKey fk = new ForeignKey();

                    fk.setMyFields(new ArrayList<>(Arrays.stream(token.split("^.*\\(|\\)[\\S\\s]*|[\", ]"))
                            .filter(str -> !str.isEmpty()).collect(Collectors.toList())));

                    fk.setForeignFields(new ArrayList<>(Arrays.stream(token.split("^.*\\([\\s\\S]*?\\(|\\)[\\s\\S]*|[\", ]"))
                            .filter(str -> !str.isEmpty()).collect(Collectors.toList())));

                    fk.setForeignTable(foreignTable);

                    foreignKeys.add(fk);
                }
            }

            ResultSet rs = DataManager.getData(tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Builds an ArrayList that contains all the columnNames to be written on the BSON
            ArrayList<ColumnMetadata> tableColumns = new ArrayList<>();
            for(int i = 1; i <= columnCount; i++)
                tableColumns.add(new ColumnMetadata(rsmd.getColumnName(i), rsmd.getColumnType(i)));

            while(rs.next()){
                for(ForeignKey currentForeign : foreignKeys) {
                    setValues(rs, currentForeign, columnCount, tableColumns);

                    ResultSet rsForeign = DataManager.getForeignTuple(currentForeign);
                    rsForeign.next();

                    BasicDBObject insertionObject = new BasicDBObject();
                    for(int i = 0; i < currentForeign.getMyFields().size(); i++)
                        insertionObject.put(currentForeign.getMyFields().get(i), rsForeign.getObject(i+1));

                    for(ForeignKey otherForeign : foreignKeys){
                        if(otherForeign == currentForeign)
                            continue;

                        // Adds the fields that aren't in either table to the insertionObject
                        for(int i = 0; i < columnCount; i++){
                            String currentColumn = tableColumns.get(i).getColumnName();
                            if(!currentForeign.getMyFields().contains(currentColumn) && !otherForeign.getMyFields().contains(currentColumn))
                                insertionObject.put(currentColumn, rs.getObject(i+1));
                        }

                        setValues(rs, otherForeign, columnCount, tableColumns);
                        BasicDBObject searchObject = new BasicDBObject();
                        BasicDBObject contents = new BasicDBObject();
                        ArrayList<String> otherPrimaries = getPrimaryFields(otherForeign.getForeignTable());

                        for(int i = 0; i < otherForeign.getMyFields().size(); i++) {
                            for (ColumnMetadata cm : tableColumns) {
                                if (Objects.equals(cm.getColumnName(), otherForeign.getMyFields().get(i))) {
                                    if(otherPrimaries.contains(otherForeign.getForeignFields().get(i))) {
                                        contents.put(otherForeign.getForeignFields().get(i), rs.getObject(cm.getColumnName()));
                                    } else {
                                        searchObject.put(otherForeign.getForeignFields().get(i), rs.getObject(cm.getColumnName()));
                                    }
                                }
                            }
                        }

                        if(contents.size() != 0)
                            searchObject.put("_id", contents);

                        String command = databaseName + "." + otherForeign.getForeignTable() + ".update(" + searchObject + ", {$addToSet: {" + currentForeign.getForeignTable() + ": " + insertionObject + "}})";
                        commandList.add(command);
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return commandList;
    }

    public ArrayList<String> buildIndexes(String tableName){
        ArrayList<String> commands = null;
        try {
            // Gets the unique columns for this table
            Multimap<String, String> constraints = DataManager.getUniqueColumns(tableName);
            commands = new ArrayList<>();

            for(String constraintName : constraints.keySet()){
                BasicDBObject indexTuple = new BasicDBObject();
                Collection<String> constraintColumns = constraints.get(constraintName);

                for(String columnName : constraintColumns)
                    indexTuple.put(columnName, 1);

                commands.add(databaseName + "." + tableName + ".createIndex(" + indexTuple + ")");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return commands;
    }

    private BasicDBObject buildDocument(ResultSet rs, ArrayList<String> primaryKeys, ArrayList<ColumnMetadata> columns) throws SQLException {
        BasicDBObject document = new BasicDBObject();
        BasicDBObject primaryKey = new BasicDBObject();

        for(int i = 0; i < columns.size(); i++) {
            if(!columns.get(i).active)
                continue;

            String currentName = columns.get(i).getColumnName();
            int currentType = columns.get(i).getColumnType();

            if (primaryKeys.contains(currentName)) {
                // This column is part of the primary key; add it to primaryKey object
                addField(primaryKey, rs, currentName, currentType, i+1);
            } else {
                // This is not part of the primary key; add it as another field on the document
                addField(document, rs, currentName, currentType, i+1);
            }
        }

        // Adds the primary key to the document
        document.put("_id", primaryKey);

        return document;
    }

    private BasicDBObject buildDocument(ResultSet rs, ArrayList<ColumnMetadata> columns) throws SQLException {
        BasicDBObject document = new BasicDBObject();

        for(int i = 0; i < columns.size(); i++) {
            if(!columns.get(i).active)
                continue;

            String currentName = columns.get(i).getColumnName();
            int currentType = columns.get(i).getColumnType();

            addField(document, rs, currentName, currentType, i+1);
        }

        return document;
    }

    private void addField(BasicDBObject object, ResultSet rs, String name, int type, int index) throws SQLException {
        Object value;
        switch (type) {
            case Types.NUMERIC:
                value = rs.getInt(index);

                if (!rs.wasNull())
                    object.put(name, value);
                break;

            case Types.CHAR:
            case Types.VARCHAR:
                value = rs.getString(index);

                if (!rs.wasNull())
                    object.put(name, value);
                break;

            case Types.TIMESTAMP:
                value = rs.getTimestamp(index);

                if (!rs.wasNull()) {
                    long timestamp = ((Timestamp) value).getTime();
                    object.put(name, new java.util.Date(timestamp));
                }
                break;
        }
    }

    private ArrayList<String> getPrimaryFields(String tableName) throws SQLException, ClassNotFoundException {
        String tableDDL = DataManager.getTableDDL(tableName);
        ArrayList<String> primaryKeys = new ArrayList<>();

        // This split gets all the constraints
        String[] constraints = tableDDL.split("CONSTRAINT");
        constraints = Arrays.copyOfRange(constraints, 1, constraints.length);

        for(String constraint : constraints){
            // This split removes the name of the constraint
            String token = constraint.split("^ *\"[^\\\"]*\" ?")[1];

            if(token.startsWith("PRIMARY KEY")){
                // Get the fields that are used as the primary key
                // Also removes any empty strings that result from the "split" function call
                primaryKeys = new ArrayList<>(Arrays.stream(token.split("^.*\\(|\\)[\\S\\s]*|[\", ]"))
                        .filter(str -> !str.isEmpty()).collect(Collectors.toList()));
            }
        }

        return primaryKeys;
    }

    private void setValues(ResultSet rs, ForeignKey fk, int columnCount, ArrayList<ColumnMetadata> tableColumns) throws SQLException {
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            if (fk.getMyFields().contains(tableColumns.get(i).getColumnName())) {
                switch (tableColumns.get(i).getColumnType()) {
                    case Types.NUMERIC:
                        values.add(Integer.toString(rs.getInt(i + 1)));
                        break;

                    case Types.CHAR:
                    case Types.VARCHAR:
                        values.add("'" + rs.getString(i + 1) + "'");
                        break;
                }
            }
        }
        fk.setValues(values);
    }
}
