package database_manager;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.Statement;

public class change_database {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:src/main/database/chinook.db";

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            // Create the Rooms table (if not exists)
            String createRoomsTable = """
                CREATE TABLE IF NOT EXISTS Rooms (
                    room_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    room_name TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'NOT_CLEANED',
                    last_cleaned_date TEXT
                );
            """;
            statement.execute(createRoomsTable);

            // Create the Tasks table (if not exists)
            String createTasksTable = """
                CREATE TABLE IF NOT EXISTS Tasks (
                    task_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    room_id INTEGER NOT NULL,
                    task_name TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'NOT_CLEANED',
                    FOREIGN KEY (room_id) REFERENCES Rooms (room_id) ON DELETE CASCADE
                );
            """;
            statement.execute(createTasksTable);

            // Insert example data into the Tasks table (optional for testing)
            String insertExampleTasks = """
                INSERT INTO Tasks (room_id, task_name, status) VALUES
                    (1, 'Bedsheets Changed', 'NOT_CLEANED'),
                    (1, 'Table Cleaned', 'NOT_CLEANED'),
                    (1, 'Vacuumed', 'NOT_CLEANED'),
                    (2, 'Windows Cleaned', 'NOT_CLEANED'),
                    (2, 'Toilet Sanitized', 'NOT_CLEANED'),
                    (2, 'Wash Basin Cleaned', 'NOT_CLEANED');
            """;
            statement.execute(insertExampleTasks);

            System.out.println("Database setup completed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
