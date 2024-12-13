package org.example.housekeeping_manager.rooms;

// Importing necessary classes
import org.example.housekeeping_manager.ElderlyHome; // The abstract parent class providing shared properties and methods
import org.example.housekeeping_manager.tasks.Task; // Represents individual tasks assigned to a room
import java.util.List; // Java's List interface to hold multiple tasks for a room

// Room class extending the abstract class ElderlyHome
public class Room extends ElderlyHome {
    private final String roomName; // Stores the name of the room
    private final List<Task> tasks; // List to store tasks associated with the room

    // Constructor to initialize Room objects
    public Room(int roomID, String roomName, String status, List<Task> tasks) {
        super(roomID, status); // Calls the constructor of the parent class (ElderlyHome) to set roomID and status
        this.roomName = roomName; // Sets the name of the room
        this.tasks = tasks; // Initializes the list of tasks associated with this room
    }

    // Getter method to retrieve the name of the room
    public String getRoomName() {
        return roomName;
    }

    // Getter method to retrieve the list of tasks for the room
    public List<Task> getTasks() {
        return tasks;
    }

    // Implementation of the abstract method from ElderlyHome to display details
    @Override
    public void displayDetails() {
        System.out.println("Room ID: " + getId()); // Prints the unique ID of the room (inherited from ElderlyHome)
        System.out.println("Room Name: " + roomName); // Prints the name of the room
        System.out.println("Status: " + getStatus()); // Prints the status of the room (e.g., CLEANED or NOT_CLEANED)
        System.out.println("Tasks: "); // Header for tasks section

        // Loops through the list of tasks and prints each task's name and status
        for (Task task : tasks) {
            System.out.println("- " + task.getTaskName() + " (" + task.getStatus() + ")");
        }
    }
}
