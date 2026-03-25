package com.minijira;

import java.util.ArrayList;
import java.util.List;

public class MyBST {
    public static class Node {
        public Task task;
        public Node left;
        public Node right;
    
        public Node(Task task) {
            this.task = task;
        }
    }
    
    private Node root;
    
    public Node getRoot() {
        return root;
    }

    public void insert(Task task) {
        root = insert(root, task);
    }

    private Node insert(Node node, Task task) {
        if (node == null) {
            return new Node(task);
        }
        if (task.getId() < node.task.getId()) {
            node.left = insert(node.left, task);
        } else if (task.getId() > node.task.getId()) {
            node.right = insert(node.right, task);
        } else {
            node.task = task;
        }
        return node;
    }

    public Task find(int id) {
        Node node = findNode(root, id);
        return node == null ? null : node.task;
    }

    private Node findNode(Node node, int id) {
        if (node == null) {
            return null;
        }
        if (id == node.task.getId()) {
            return node;
        }
        if (id < node.task.getId()) {
            return findNode(node.left, id);
        }
        return findNode(node.right, id);
    }

    public List<Task> inOrder() {
        List<Task> tasks = new ArrayList<>();
        traverse(root, tasks);
        return tasks;
    }

    private void traverse(Node node, List<Task> tasks) {
        if (node == null) {
            return;
        }
        traverse(node.left, tasks);
        tasks.add(node.task);
        traverse(node.right, tasks);
    }

    public void clear() {
        root = null;
    }
}
