package database_manager;

        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;

public class database_connection{

    private static final String DB_URL = "jdbc:sqlite:housekeeping_manager.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
