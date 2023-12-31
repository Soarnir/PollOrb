package pollorb.database;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
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
    private static QueryRunner queryRunner;
    private static Connection connection;

    /**
     * Initialize the database manager
     */
    public static void initialize() throws SQLException {
        logger.info("Initializing Database Manager");
        connection = DriverManager.getConnection(url, username, password);
        queryRunner = new QueryRunner();
    }

    public static void testConnection() throws SQLException {
        logger.debug("Testing connection");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM guilds");
        while (resultSet.next()) {
            logger.debug("Rows: " + resultSet.getString(1));
        }
        resultSet.close();
        statement.close();
    }

    public static void query(String sql) {
        try {
            logger.debug("Running SQL: " + sql);
            logger.debug("Rows updated: " + queryRunner.update(connection, sql));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <Type> Type select(String sql, ResultSetHandler<Type> resultSetHandler) {
        try {
            logger.debug("Running SQL: " + sql);
            return queryRunner.query(connection, sql, resultSetHandler);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
