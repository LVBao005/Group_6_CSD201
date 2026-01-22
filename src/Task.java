/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
public class Task {
    String id;
    String title;
    String priority; // High, Medium, Low

    public Task(String id, String title, String priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return String.format("[ID: %s | %s | %s]", id, title, priority);
    }
}
    

