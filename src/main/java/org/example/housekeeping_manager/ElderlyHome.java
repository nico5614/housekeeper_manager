package org.example.housekeeping_manager;

public abstract class ElderlyHome {
    private final int id;       // Unique identifier
    private String status;      // CLEANED or NOT_CLEANED

    // Constructor
    public ElderlyHome(int id, String status) {
        this.id = id;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Abstract method to display details (to be implemented by subclasses)
    public abstract void displayDetails();
}
