package org.example.housekeeping_manager.database_manager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class example_database {

    private static final String DATABASE_URL = "jdbc:sqlite:src/main/database/chinook.db";

    public static void main(String[] args) {
        addExampleRooms();
    }

    public static void addExampleRooms() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            System.out.println("Connected to the SQLite database.");

            // Insert example data
            insertRoom(connection, "Zimmer 101", "CLEANED", getCurrentDate());
            insertRoom(connection, "Zimmer 102", "NOT_CLEANED", "2024-11-30");

            System.out.println("Example rooms added successfully.");
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting example rooms: " + e.getMessage());
        }
    }

    private static void insertRoom(Connection connection, String roomName, String status, String lastCleanedDate) {
        String insertSQL = """
            INSERT INTO Rooms (RoomName, Status, LastCleanedDate)
            VALUES (?, ?, ?);
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, roomName);
            pstmt.setString(2, status);
            pstmt.setString(3, lastCleanedDate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting room: " + e.getMessage());
        }
    }

    private static String getCurrentDate() {
        // Get the current date in ISO format (YYYY-MM-DD)
        java.time.LocalDate today = java.time.LocalDate.now();
        return today.toString();
    }
}

