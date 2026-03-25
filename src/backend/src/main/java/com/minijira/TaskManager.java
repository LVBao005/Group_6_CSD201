package com.minijira;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaskManager {
    private static final Map<String, MyLinkedList> columns = new LinkedHashMap<>();
    private static final MyBST bst = new MyBST();
    private static final MyGraph graph = new MyGraph();
    private static final TaskRepository repository = new TaskRepository();
    private static boolean initialized = false;

    static {
        columns.put("TODO", new MyLinkedList());
        columns.put("DOING", new MyLinkedList());
        columns.put("DONE", new MyLinkedList());
    }

    public static synchronized void ensureInitialized() {
        if (!initialized) {
            reload();
        }
    }

    public static synchronized void reload() {
        List<Task> tasks = repository.findAll();
        rebuild(tasks);
        initialized = true;
    }

    private static void rebuild(List<Task> tasks) {
        bst.clear();
        graph.clear();
        columns.values().forEach(MyLinkedList::clear);

        for (Task task : tasks) {
            bst.insert(task);
            String column = normalize(task.getStatus());
            columns.computeIfAbsent(column, k -> new MyLinkedList()).add(task);
            graph.addTask(task.getId());
            if (task.getDependsOn() != null) {
                graph.addDependency(task.getDependsOn(), task.getId());
            }
        }
    }

    public static synchronized Map<String, List<Task>> snapshot() {
        ensureInitialized();
        Map<String, List<Task>> result = new LinkedHashMap<>();
        for (Map.Entry<String, MyLinkedList> entry : columns.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toList());
        }
        return result;
    }

    public static synchronized Map<Integer, List<Integer>> dependencySnapshot() {
        ensureInitialized();
        return graph.snapshot();
    }

    public static synchronized void shuffleBST() {
        ensureInitialized();
        List<Task> tasks = repository.findAll();
        Collections.shuffle(tasks);
        bst.clear();
        for (Task t : tasks) {
            bst.insert(t);
        }
    }

    public static synchronized boolean moveTask(int taskId, String targetColumn) {
        ensureInitialized();
        reload(); // keep data consistent with underlying DB
        String destination = normalize(targetColumn);
        if (!columns.containsKey(destination)) {
            return false;
        }
        Task task = bst.find(taskId);
        if (task == null) {
            return false;
        }
        String previous = normalize(task.getStatus());
        MyLinkedList sourceList = columns.get(previous);
        if (sourceList != null) {
            sourceList.remove(taskId);
        }
        task.setStatus(destination);
        columns.get(destination).add(task);
        boolean updated = repository.updateStatus(taskId, destination);
        if (!updated) {
            System.err.println("Could not persist move for task " + taskId);
        } else {
            reload();
        }
        return true;
    }

    public static synchronized Task findById(int taskId) {
        ensureInitialized();
        return bst.find(taskId);
    }

    public static synchronized boolean hasDependencyCycle() {
        ensureInitialized();
        return graph.hasCycle();
    }

    public static synchronized BalancedNode buildActualTree() {
        ensureInitialized();
        return convertToBalancedNode(bst.getRoot());
    }

    private static BalancedNode convertToBalancedNode(MyBST.Node node) {
        if (node == null) {
            return null;
        }
        BalancedNode result = new BalancedNode(node.task);
        result.left = convertToBalancedNode(node.left);
        result.right = convertToBalancedNode(node.right);
        return result;
    }


    public static synchronized Set<Integer> blockingParents(int taskId) {
        ensureInitialized();
        MyLinkedList doneList = columns.get("DONE");
        return graph.blockedParents(taskId, doneList);
    }

    public static String normalize(String raw) {
        if (raw == null) {
            return "TODO";
        }
        return raw.trim().toUpperCase();
    }

    public static class BalancedNode {
        private final int id;
        private final String title;
        private final String status;
        private final String priority;
        private final int estimateHours;
        private final Integer dependsOn;
        private BalancedNode left;
        private BalancedNode right;

        private BalancedNode(Task task) {
            this.id = task.getId();
            this.title = task.getTitle();
            this.status = task.getStatus();
            this.priority = task.getPriority();
            this.estimateHours = task.getEstimateHours();
            this.dependsOn = task.getDependsOn();
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getStatus() {
            return status;
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

        public BalancedNode getLeft() {
            return left;
        }

        public BalancedNode getRight() {
            return right;
        }
    }
}
