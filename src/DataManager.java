import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataManager {
    /**
     * Simply returns the DDL of the table specified
     */
    public static String getTableDDL(String tableName) throws SQLException, ClassNotFoundException {
        Connection connection = OracleConnector.getConnection();
        Statement statement = connection.createStatement();

        String query = "SELECT DBMS_METADATA.GET_DDL('TABLE', TABLE_NAME) AS TABLE_DDL FROM USER_TABLES NATURAL JOIN USER_SYNONYMS WHERE SYNONYM_NAME = '" + tableName + "'" ;

        ResultSet rs = statement.executeQuery(query);
        rs.next();
        return rs.getString("TABLE_DDL");
    }

    /**
     * Simple select from a table to build the BSON
     */
    public static ResultSet getData(String tableName) throws SQLException, ClassNotFoundException {
        Connection connection = OracleConnector.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + tableName;

        return statement.executeQuery(query);
    }

    /**
     * Gets all the tuples that match the values passed with the ForeignKey parameter
     */
    public static ResultSet getForeignTuple(ForeignKey foreignKey) throws SQLException, ClassNotFoundException {
        Connection connection = OracleConnector.getConnection();
        Statement statement = connection.createStatement();

        String subquery = "";
        for(int i = 0; i < foreignKey.getForeignFields().size(); i++) {
            subquery += foreignKey.getForeignFields().get(i) + " = " + foreignKey.getValues().get(i);

            if(i < foreignKey.getForeignFields().size() - 1)
                subquery += " AND ";
        }

        String query = "SELECT * FROM " + foreignKey.getForeignTable() + " WHERE " + subquery;

        return statement.executeQuery(query);
    }

    public static Multimap<String, String> getUniqueColumns(String tableName) throws SQLException, ClassNotFoundException {
        Connection connection = OracleConnector.getConnection();
        Statement statement = connection.createStatement();

        String query = "SELECT COLS.CONSTRAINT_NAME, COLS.COLUMN_NAME FROM USER_CONSTRAINTS CONS JOIN USER_CONS_COLUMNS COLS ON (CONS.OWNER = COLS.OWNER AND CONS.CONSTRAINT_NAME = COLS.CONSTRAINT_NAME) JOIN USER_SYNONYMS SYMS ON CONS.TABLE_NAME = SYMS.TABLE_NAME WHERE CONS.CONSTRAINT_TYPE = 'U' AND SYMS.SYNONYM_NAME = '" + tableName + "' ORDER BY COLS.POSITION";
        ResultSet rs = statement.executeQuery(query);

        Multimap<String, String> constraints = ArrayListMultimap.create();
        while(rs.next())
            constraints.put(rs.getString("CONSTRAINT_NAME"), rs.getString("COLUMN_NAME"));

        return constraints;
    }
}
