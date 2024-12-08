package database_manager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class database_reader {

    private static final String DB_URL = "jdbc:sqlite:src/main/database/chinook.db";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            System.out.println("Connected to the database.");

            // List all tables
            System.out.println("\n--- Database Tables ---");
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    System.out.println("Table: " + tableName);

                    // List columns for the current table
                    System.out.println("\t--- Columns in " + tableName + " ---");
                    try (ResultSet columns = metaData.getColumns(null, null, tableName, "%")) {
                        while (columns.next()) {
                            String columnName = columns.getString("COLUMN_NAME");
                            String columnType = columns.getString("TYPE_NAME");
                            System.out.println("\t" + columnName + " (" + columnType + ")");
                        }
                    }

                    // Display table content
                    System.out.println("\t--- Data in " + tableName + " ---");
                    try (Statement statement = connection.createStatement();
                         ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {
                        int columnCount = resultSet.getMetaData().getColumnCount();
                        while (resultSet.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                System.out.print(resultSet.getMetaData().getColumnName(i) + ": " + resultSet.getString(i) + " | ");
                            }
                            System.out.println();
                        }
                    }
                }
            }

            // Verify Rooms and Tasks Relationship
            System.out.println("\n--- Verifying Tasks and Rooms Relationship ---");
            String query = """
                SELECT Rooms.RoomName, Tasks.TaskName, Tasks.Status
                FROM Tasks
                JOIN Rooms ON Tasks.RoomID = Rooms.RoomID
                ORDER BY Rooms.RoomName, Tasks.TaskName;
            """;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    String roomName = resultSet.getString("RoomName");
                    String taskName = resultSet.getString("TaskName");
                    String taskStatus = resultSet.getString("Status");
                    System.out.println("Room: " + roomName + " | Task: " + taskName + " | Status: " + taskStatus);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
