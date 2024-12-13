package org.example.housekeeping_manager;

import org.example.housekeeping_manager.rooms.Room;
import org.example.housekeeping_manager.tasks.Task;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedList;

/**
 name -> System.out.println(name): This is a lambda expression.
 It means: For each name in the list, execute the block System.out.println(name).
 */


public class manager_functions {

    // Logger for debugging and logging information
    private static final Logger logger = LoggerFactory.getLogger(manager_functions.class);

    // LinkedList to track the history of recent task updates
    private static LinkedList<String> taskHistory = new LinkedList<>();

    /**
     * Fetches all rooms and their associated tasks from the database.
     *
     * - Queries the database for all rooms and their tasks.
     * - Logs room and task details for debugging.
     *
     * @return List of Room objects with associated tasks.
     */
    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String roomQuery = "SELECT * FROM Rooms";
        String taskQuery = "SELECT * FROM Tasks WHERE RoomID = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/database/chinook.db");
             PreparedStatement roomStatement = connection.prepareStatement(roomQuery);
             PreparedStatement taskStatement = connection.prepareStatement(taskQuery)) {

            logger.debug("Executing query: {}", roomQuery);
            ResultSet roomResultSet = roomStatement.executeQuery();

            while (roomResultSet.next()) {
                int roomID = roomResultSet.getInt("RoomID");
                String roomName = roomResultSet.getString("RoomName");
                String status = roomResultSet.getString("Status");

                // Optional logging for specific room ID (debugging purposes)
                if (roomID == 1) {
                    logger.info("Room found: Name={} Status={}", roomName, status);
                }

                // Fetch tasks associated with the current room
                List<Task> tasks = new ArrayList<>();
                taskStatement.setInt(1, roomID);
                ResultSet taskResultSet = taskStatement.executeQuery();
                while (taskResultSet.next()) {
                    int taskID = taskResultSet.getInt("TaskID");
                    String taskName = taskResultSet.getString("TaskName");
                    String taskStatus = taskResultSet.getString("Status");

                    // Optional logging for debugging tasks of a specific room
                    if (roomID == 1) {
                        logger.info("Task for Room: ID={} Name={} Status={}", taskID, taskName, taskStatus);
                    }

                    tasks.add(new Task(taskID, taskName, taskStatus));
                }

                // Add the room and its tasks to the list
                rooms.add(new Room(roomID, roomName, status, tasks));
            }
        } catch (SQLException e) {
            logger.error("SQL error in getAllRooms", e);
        }
        return rooms;
    }

    /**
     * Updates the status of a specific task and logs the change in the task history.
     *
     * - Updates the task status in the database.
     * - Adds the change to the task history list.
     * - Limits the task history to the last 6 changes.
     *
     * @param taskID ID of the task to update.
     * @param newStatus New status for the task.
     */
    public static void updateTaskStatus(int taskID, String newStatus) {
        String sql = "UPDATE Tasks SET Status = ? WHERE TaskID = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/database/chinook.db");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Update the task status in the database
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, taskID);
            preparedStatement.executeUpdate();

            // Log the change and update the task history
            List<Room> rooms = getAllRooms();
            for (Room room : rooms) {
                for (Task task : room.getTasks()) {
                    if (task.getId() == taskID) {
                        String historyEntry = String.format(
                                "In Room '%s', Task '%s' changed status to '%s'",
                                room.getRoomName(), task.getTaskName(), newStatus
                        );
                        taskHistory.addFirst(historyEntry);

                        // Keep only the last 6 history entries
                        if (taskHistory.size() > 6) {
                            taskHistory.removeLast();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("SQL error in updateTaskStatus", e);
        }
    }

    /**
     * Checks if all tasks for a specific room are marked as "CLEANED".
     *
     * @param roomID ID of the room to check.
     * @return True if all tasks are marked as "CLEANED", otherwise false.
     */
    public static boolean isRoomClean(int roomID) {
        String sql = "SELECT COUNT(*) AS NotCleanedTasks FROM Tasks WHERE RoomID = ? AND Status = 'NOT_CLEANED'";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/database/chinook.db");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roomID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("NotCleanedTasks") == 0;
            }
        } catch (SQLException e) {
            logger.error("SQL error in isRoomClean", e);
        }
        return false;
    }

    /**
     * Updates the status of a room based on the statuses of its tasks.
     *
     * - Marks the room as "CLEANED" if all tasks are cleaned.
     * - Marks the room as "NOT_CLEANED" otherwise.
     *
     * @param roomID ID of the room to update.
     */
    public static void updateRoomStatusBasedOnTasks(int roomID) {
        boolean isClean = isRoomClean(roomID);
        updateRoomStatus(roomID, isClean ? "CLEANED" : "NOT_CLEANED");
    }

    /**
     * Updates the status and last cleaned date of a room in the database.
     *
     * @param roomID ID of the room to update.
     * @param newStatus New status for the room.
     */
    public static void updateRoomStatus(int roomID, String newStatus) {
        String sql = "UPDATE Rooms SET Status = ?, LastCleanedDate = ? WHERE RoomID = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/database/chinook.db");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setString(2, LocalDate.now().toString());
            preparedStatement.setInt(3, roomID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQL error in updateRoomStatus", e);
        }
    }

    /**
     * Retrieves the recent task history.
     *
     * @return LinkedList containing the task history.
     */
    public static LinkedList<String> getTaskHistory() {
        return taskHistory;
    }
}
