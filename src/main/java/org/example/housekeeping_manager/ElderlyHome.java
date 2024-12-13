package org.example.housekeeping_manager;

// Abstract class representing shared properties and behavior for entities in the system
public abstract class ElderlyHome {
    private final int id; // Unique identifier for the entity (e.g., a room or a task)
    private String status; // Status of the entity (e.g., "CLEANED", "NOT_CLEANED")

    // Constructor to initialize the ID and status fields
    public ElderlyHome(int id, String status) {
        this.id = id; // Sets the unique ID
        this.status = status; // Sets the initial status
    }

    // Getter method to retrieve the unique ID
    public int getId() {
        return id;
    }

    // Getter method to retrieve the current status
    public String getStatus() {
        return status;
    }

    // Setter method to update the status
    public void setStatus(String status) {
        this.status = status;
    }

    // Abstract method that must be implemented by subclasses
    // Ensures every subclass provides its own way of displaying details
    public abstract void displayDetails();
}
