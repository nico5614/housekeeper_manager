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

public class manager_functions {

    private static final Logger logger = LoggerFactory.getLogger(manager_functions.class);
    // Fetch all rooms along with their tasks from the database
    private static LinkedList<String> taskHistory = new LinkedList<>();

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

                // Log details only for Room ID 1
if (roomID == 1) {
   // logger.info("Room / s found: Name={} Status={}", roomName, status);
}
                // Fetch tasks for Room ID 1
                List<Task> tasks = new ArrayList<>();
                taskStatement.setInt(1, roomID);
                ResultSet taskResultSet = taskStatement.executeQuery();
                while (taskResultSet.next()) {
                    int taskID = taskResultSet.getInt("TaskID");
                    String taskName = taskResultSet.getString("TaskName");
                    String taskStatus = taskResultSet.getString("Status");

                    // Log task details only for Room ID 1 // if (roomID == 1) {}
if (roomID == 1) {
   // logger.info("Task for Room / s: ID={} Name={} Status={}", taskID, taskName, taskStatus);
                }

                    tasks.add(new Task(taskID, taskName, taskStatus));
                }

                rooms.add(new Room(roomID, roomName, status, tasks));
            }
        } catch (SQLException e) {
            logger.error("SQL error in getAllRooms", e);
        }
        return rooms;
    }


    public static void updateTaskStatus(int taskID, String newStatus) {
        String sql = "UPDATE Tasks SET Status = ? WHERE TaskID = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/database/chinook.db");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Execute the database update
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, taskID);
            preparedStatement.executeUpdate();

            // Fetch the task details for logging
            List<Room> rooms = getAllRooms(); // Assume getAllRooms fetches the latest rooms from the database
            for (Room room : rooms) {
                for (Task task : room.getTasks()) {
                    if (task.getId() == taskID) {
                        task.setStatus(newStatus); // Update the task's status in memory

                        // Add to task history with only the end status
                        addTaskHistory("Task '" + task.getTaskName() + "' in Room '" + room.getRoomName() +
                                "' changed to " + newStatus);

                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    // Check if all tasks for a specific room are marked as "CLEANED"
    public static boolean isRoomClean(int roomID) {
        String sql = "SELECT COUNT(*) AS NotCleanedTasks FROM Tasks WHERE RoomID = ? AND Status = 'NOT_CLEANED'";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/database/chinook.db");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roomID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("NotCleanedTasks") == 0; // If no tasks are "NOT_CLEANED", the room is clean
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update the room status based on the tasks' statuses
    public static void updateRoomStatusBasedOnTasks(int roomID) {
        boolean isClean = isRoomClean(roomID);
        updateRoomStatus(roomID, isClean ? "CLEANED" : "NOT_CLEANED");
    }

    // Update a room's status and last cleaned date
    public static void updateRoomStatus(int roomID, String newStatus) {
        String sql = "UPDATE Rooms SET Status = ?, LastCleanedDate = ? WHERE RoomID = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/database/chinook.db");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setString(2, LocalDate.now().toString());
            preparedStatement.setInt(3, roomID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTaskHistory(String taskChange) {
        taskHistory.addFirst(taskChange); // Add the latest change at the beginning
        if (taskHistory.size() > 6) { // Keep only the last 6 changes
            taskHistory.removeLast(); // Remove the oldest entry
        }

    }
        public static LinkedList<String> getTaskHistory() {
            return taskHistory;
        }

}




