package org.example.housekeeping_manager.rooms;

import org.example.housekeeping_manager.ElderlyHome;
import org.example.housekeeping_manager.tasks.Task;
import java.util.List;

public class Room extends ElderlyHome {
    private final String roomName;
    private final List<Task> tasks; // List of tasks for the room

    // Constructor
    public Room(int roomID, String roomName, String status, List<Task> tasks) {
        super(roomID, status);  // Call parent constructor
        this.roomName = roomName;
        this.tasks = tasks;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public void displayDetails() {
        System.out.println("Room ID: " + getId());
        System.out.println("Room Name: " + roomName);
        System.out.println("Status: " + getStatus());
        System.out.println("Tasks: ");
        for (Task task : tasks) {
            System.out.println("- " + task.getTaskName() + " (" + task.getStatus() + ")");
        }
    }
}
