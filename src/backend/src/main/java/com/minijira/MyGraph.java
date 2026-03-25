package com.minijira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MyGraph {
    private final Map<Integer, Set<Integer>> adjacency = new HashMap<>();

    public void addTask(int taskId) {
        adjacency.computeIfAbsent(taskId, k -> new HashSet<>());
    }

    public void addDependency(int prerequisiteId, int dependentId) {
        addTask(prerequisiteId);
        addTask(dependentId);
        adjacency.get(prerequisiteId).add(dependentId);
    }

    public Set<Integer> predecessors(int taskId) {
        Set<Integer> parents = new HashSet<>();
        for (Map.Entry<Integer, Set<Integer>> entry : adjacency.entrySet()) {
            if (entry.getValue().contains(taskId)) {
                parents.add(entry.getKey());
            }
        }
        return parents;
    }

    public Set<Integer> blockedParents(int taskId, MyLinkedList doneList) {
        Set<Integer> parents = predecessors(taskId);
        if (doneList == null) {
            return parents;
        }
        return parents.stream().filter(parent -> !doneList.contains(parent)).collect(Collectors.toSet());
    }

    public boolean hasCycle() {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> stack = new HashSet<>();
        for (Integer node : adjacency.keySet()) {
            if (!visited.contains(node) && detectCycle(node, visited, stack)) {
                return true;
            }
        }
        return false;
    }

    private boolean detectCycle(Integer node, Set<Integer> visited, Set<Integer> stack) {
        visited.add(node);
        stack.add(node);
        for (Integer neighbor : adjacency.getOrDefault(node, new HashSet<>())) {
            if (!visited.contains(neighbor)) {
                if (detectCycle(neighbor, visited, stack)) {
                    return true;
                }
            } else if (stack.contains(neighbor)) {
                return true;
            }
        }
        stack.remove(node);
        return false;
    }

    public Map<Integer, List<Integer>> snapshot() {
        Map<Integer, List<Integer>> spread = new HashMap<>();
        for (Map.Entry<Integer, Set<Integer>> entry : adjacency.entrySet()) {
            spread.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return spread;
    }

    public void clear() {
        adjacency.clear();
    }
}
