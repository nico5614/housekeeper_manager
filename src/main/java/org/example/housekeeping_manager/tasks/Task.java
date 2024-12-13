package org.example.housekeeping_manager.tasks;

import org.example.housekeeping_manager.ElderlyHome;

public class Task extends ElderlyHome {
    private final String taskName; // Name of the task

    // Constructor
    public Task(int taskID, String taskName, String status) {
        super(taskID, status);  // Call parent constructor
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    @Override
    public void displayDetails() {
        System.out.println("Task ID: " + getId());
        System.out.println("Task Name: " + taskName);
        System.out.println("Status: " + getStatus());
    }
}