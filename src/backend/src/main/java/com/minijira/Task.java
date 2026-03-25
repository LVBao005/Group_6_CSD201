package com.minijira;

public class Task {
    private final int id;
    private final String title;
    private final String description;
    private String status;
    private final String priority;
    private final int estimateHours;
    private final Integer dependsOn;

    public Task(int id, String title, String description, String status, String priority, int estimateHours, Integer dependsOn) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.estimateHours = estimateHours;
        this.dependsOn = dependsOn;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public int getEstimateHours() {
        return estimateHours;
    }

    public Integer getDependsOn() {
        return dependsOn;
    }
}
