package org.example.housekeeping_manager;

public abstract class ElderlyHome {
    private final int id;
    private String status;

    // Constructor
    public ElderlyHome(int id, String status) {
        this.id = id;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public abstract void displayDetails();
}
