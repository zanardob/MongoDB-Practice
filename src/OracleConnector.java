import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Simple class for connecting into the Oracle database
 * The username and password need to be provided with setCredentials()
 */
public class OracleConnector {
    private static final String CONNECTION = "jdbc:oracle:thin:@grad.icmc.usp.br:15215:orcl";
    private static String username = null;
    private static String password = null;
    private static Connection connection = null;

    private static Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        connection = DriverManager.getConnection(CONNECTION, username, password);

        return connection;
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if(connection != null && !connection.isClosed()) {
            return connection;
        }

        return connect();
    }

    public static void setCredentials(String username, String password){
        OracleConnector.username = username;
        OracleConnector.password = password;
    }
}
