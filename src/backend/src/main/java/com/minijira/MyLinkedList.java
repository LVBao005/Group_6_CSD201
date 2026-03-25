package com.minijira;

import java.util.ArrayList;
import java.util.List;

public class MyLinkedList {
    private static class Node {
        final Task task;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    private Node head;

    public void add(Task task) {
        Node node = new Node(task);
        if (head == null) {
            head = node;
            return;
        }
        Node iterator = head;
        while (iterator.next != null) {
            iterator = iterator.next;
        }
        iterator.next = node;
    }

    public Task remove(int taskId) {
        Node prev = null;
        Node current = head;
        while (current != null) {
            if (current.task.getId() == taskId) {
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                current.next = null;
                return current.task;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    public boolean contains(int taskId) {
        Node current = head;
        while (current != null) {
            if (current.task.getId() == taskId) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public List<Task> toList() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    public void clear() {
        head = null;
    }
}
