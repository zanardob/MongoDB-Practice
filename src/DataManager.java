import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataManager {
    /**
     * Simply returns the DDL of the table specified
     */
    public static String getTableDDL(String tableName) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseConnector.getConnection();
        Statement statement = connection.createStatement();

        String query = "SELECT DBMS_METADATA.GET_DDL('TABLE', u.table_name) AS TABLE_DDL FROM USER_TABLES u WHERE u.TABLE_NAME = '" + tableName + "'";

        ResultSet rs = statement.executeQuery(query);
        rs.next();
        return rs.getString("TABLE_DDL");
    }

    /**
     * Simple select from a table to build the BSON
     */
    public static ResultSet getData(String tableName) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseConnector.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + tableName;

        return statement.executeQuery(query);
    }
}
