package org.example.housekeeping_manager.rooms;
import org.example.housekeeping_manager.tasks.Task;

import java.util.List;

public class Room {
    private final int roomID;
    private final String roomName;
    private final String status;
    private final List<Task> tasks; // Field to store tasks for the room

    // Constructor
    public Room(int roomID, String roomName, String status, List<Task> tasks) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.status = status;
        this.tasks = tasks;
    }

    // Getters
    public int getRoomID() {return roomID;}
    public String getRoomName() {
        return roomName;
    }
    public String getStatus() {
        return status;
    }
    public List<Task> getTasks() {
        return tasks;
    }
}
