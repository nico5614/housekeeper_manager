package org.example.housekeeping_manager.tasks;

// Importing the abstract base class
import org.example.housekeeping_manager.ElderlyHome;

// Task class that extends ElderlyHome, representing a specific housekeeping task
public class Task extends ElderlyHome {
    private final String taskName; // Field to store the name of the task (e.g., "Vacuum room")

    // Constructor to initialize a Task object
    public Task(int taskID, String taskName, String status) {
        super(taskID, status); // Calls the parent class (ElderlyHome) constructor to set taskID and status
        this.taskName = taskName; // Sets the name of the task
    }

    // Getter method to retrieve the task name
    public String getTaskName() {
        return taskName;
    }

    // Implementation of the abstract method from ElderlyHome to display task details
    @Override
    public void displayDetails() {
        System.out.println("Task ID: " + getId()); // Prints the task's unique ID (from ElderlyHome)
        System.out.println("Task Name: " + taskName); // Prints the name of the task
        System.out.println("Status: " + getStatus()); // Prints the current status of the task (e.g., CLEANED, NOT_CLEANED)
    }
}
