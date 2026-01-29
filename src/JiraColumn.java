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

    public String getColumnName() {
        return columnName;
    }

    // Add a task to the end of the list (Append)
    public void addTask(Task task) {
        if (task == null)
            return; // Null safety
        TaskNode newNode = new TaskNode(task);
        if (head == null) {
            head = newNode;
            return;
        }
        TaskNode current = head;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        current.setNext(newNode);
    }

    // Find and remove a task by ID (for moving tasks between columns)
    public Task findAndRemoveTask(String taskId) {
        if (head == null || taskId == null)
            return null;

        // Case 1: Head is the target
        if (head.getData() != null && taskId.equals(head.getData().getId())) {
            Task removedData = head.getData();
            head = head.getNext();
            return removedData;
        }

        // Case 2: Target is in the middle or end
        TaskNode current = head;
        while (current.getNext() != null) {
            if (current.getNext().getData() != null && taskId.equals(current.getNext().getData().getId())) {
                Task removedData = current.getNext().getData();
                current.setNext(current.getNext().getNext());
                return removedData;
            }
            current = current.getNext();
        }
        return null;
    }

    // Remove the first task (Head removal)
    public void removeFirst() {
        if (head == null) {
            return; // Silent fail for null safety
        }
        head = head.getNext();
    }

    // Remove the last task (Tail removal)
    public void removeLast() {
        // Case 1: List is empty
        if (head == null)
            return;

        // Case 2: List has only one element
        if (head.getNext() == null) {
            head = null;
            return;
        }

        // Case 3: List has multiple elements
        TaskNode current = head;
        while (current.getNext().getNext() != null) {
            current = current.getNext();
        }
        current.setNext(null);
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
            System.out.println(" -> " + current.getData());
            current = current.getNext();
        }
    }
}