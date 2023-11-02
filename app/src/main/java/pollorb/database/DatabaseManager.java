package pollorb.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class DatabaseManager {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    public static String url;
    public static String username;
    public static String password;
    private static Connection connection;

    /**
     * Initialize the database manager
     */
    public static void initialize() throws SQLException {
        logger.info("Initializing Database Manager");
        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
    }


    public static void testConnection() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM guilds");
        while (resultSet.next()) {
            logger.info("Rows: " + resultSet.getString(1));
        }
        resultSet.close();
        statement.close();
    }

    public void generateTables() {

    }
/*
    public boolean create() {

    }

    public boolean read() {

    }

    public boolean update() {

    }

    public boolean delete() {

    }*/
}
