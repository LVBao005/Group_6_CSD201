/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
/**
 * Represents a Column in Jira (e.g., To Do, In Progress, Done).
 * Implemented using a custom Singly Linked List.
 */
public class JiraColumn {
    private String columnName;
    private TaskNode head;

    public JiraColumn(String name) {
        this.columnName = name;
        this.head = null;
    }

    // Add a task to the end of the list (Append)
    public void addTask(Task task) {
        TaskNode newNode = new TaskNode(task);
        if (head == null) {
            head = newNode;
            return;
        }
        TaskNode current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }

    // Remove the first task (Head removal)
    public void removeFirst() {
        if (head == null) {
            System.out.println("Warning: Column '" + columnName + "' is empty. Nothing to remove.");
            return;
        }
        head = head.next;
    }

    // Remove the last task (Tail removal)
    public void removeLast() {
        // Case 1: List is empty
        if (head == null) return;
        
        // Case 2: List has only one element
        if (head.next == null) {
            head = null;
            return;
        }
        
        // Case 3: List has multiple elements
        TaskNode current = head;
        while (current.next.next != null) {
            current = current.next;
        }
        current.next = null;
    }

    // Display all tasks in the console
    public void displayTasks() {
        System.out.println("\n--- JIRA COLUMN: " + columnName.toUpperCase() + " ---");
        if (head == null) {
            System.out.println("[Empty]");
            return;
        }
        TaskNode current = head;
        while (current != null) {
            System.out.println(" -> " + current.data);
            current = current.next;
        }
    }
}