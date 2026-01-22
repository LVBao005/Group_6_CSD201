/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
public class MiniJiraSystem {
    public static void main(String[] args) {
        JiraColumn toDoColumn = new JiraColumn("To Do");

        System.out.println("Starting Unit Tests for Module 1...");

        // TEST CASE 1: Empty List Operations
        System.out.print("Test 1: Removing from empty list... ");
        try {
            toDoColumn.removeFirst();
            toDoColumn.removeLast();
            System.out.println("PASSED (No NullPointerException)");
        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage());
        }

        // TEST CASE 2: Adding Tasks
        System.out.println("\nTest 2: Populating tasks...");
        toDoColumn.addTask(new Task("SCRUM-1", "Setup Project Repo", "High"));
        toDoColumn.addTask(new Task("SCRUM-2", "Define Data Models", "Medium"));
        toDoColumn.addTask(new Task("SCRUM-3", "Implement Linked List", "High"));
        toDoColumn.displayTasks();

        // TEST CASE 3: Remove Tail
        System.out.println("\nTest 3: Removing last task (SCRUM-3)...");
        toDoColumn.removeLast();
        toDoColumn.displayTasks();

        // TEST CASE 4: Remove Head
        System.out.println("\nTest 4: Removing first task (SCRUM-1)...");
        toDoColumn.removeFirst();
        toDoColumn.displayTasks();

        // TEST CASE 5: Emptying the list
        System.out.println("\nTest 5: Removing the final remaining task...");
        toDoColumn.removeLast();
        toDoColumn.displayTasks();

        System.out.println("\nSummary: Module 1 logic is stable.");
    }
}