package database_manager;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.sql.ResultSet;

public class setup_database {

    private static final String DATABASE_URL = "jdbc:sqlite:src/main/database/chinook.db";

    public static void main(String[] args) {
        setupDatabase();
    }

    public static void setupDatabase() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            System.out.println("Connected to the SQLite database.");

            // Step 1: Clear all tables except sqlite_master and sqlite_sequence
            clearDatabase(connection);

            // Step 2: Create the Rooms table
            createRoomsTable(connection);

        } catch (SQLException e) {
            System.err.println("An error occurred while setting up the database: " + e.getMessage());
        }
    }

    private static void clearDatabase(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            // Query to get all table names except sqlite system tables
            String getTablesQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT IN ('sqlite_master', 'sqlite_sequence');";
            try (ResultSet resultSet = stmt.executeQuery(getTablesQuery)) {
                while (resultSet.next()) {
                    String tableName = resultSet.getString("name");
                    System.out.println("Dropping table: " + tableName);

                    // Drop the table
                    stmt.execute("DROP TABLE IF EXISTS " + tableName + ";");
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while clearing the database: " + e.getMessage());
        }
    }

    private static void createRoomsTable(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            // SQL to create the Rooms table
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS Rooms (
                    RoomID INTEGER PRIMARY KEY AUTOINCREMENT,
                    RoomName TEXT NOT NULL,
                    Status TEXT NOT NULL CHECK(Status IN ('CLEANED', 'NOT_CLEANED')),
                    LastCleanedDate TEXT NOT NULL
                );
            """;

            stmt.execute(createTableSQL);
            System.out.println("Rooms table created successfully.");
        } catch (SQLException e) {
            System.err.println("An error occurred while creating the Rooms table: " + e.getMessage());
        }
    }
}
