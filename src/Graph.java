import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {
    // Edge class to store destination and weight
    static class Edge {
        String destination;
        int weight;

        public Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    // Adjacency list representing the graph
    private Map<String, List<Edge>> adjList;

    public Graph() {
        this.adjList = new HashMap<>();
    }

    // Add a directed edge to the graph (for undirected, call this twice with swapped source/dest)
    public void addEdge(String source, String destination, int weight) {
        adjList.putIfAbsent(source, new ArrayList<>());
        adjList.putIfAbsent(destination, new ArrayList<>()); // Ensure destination exists in the map
        adjList.get(source).add(new Edge(destination, weight));
    }

    // Add an undirected edge
    public void addUndirectedEdge(String source, String destination, int weight) {
        addEdge(source, destination, weight);
        addEdge(destination, source, weight);
    }

    // Read graph data from a text file
    // Format each line: source destination weight
    // e.g., A B 5
    public void readFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\s+");
                if (parts.length >= 3) {
                    String source = parts[0];
                    String dest = parts[1];
                    try {
                        int weight = Integer.parseInt(parts[2]);
                        // Assuming undirected graph for map routing
                        addUndirectedEdge(source, dest, weight);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid weight format in line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    // Dijkstra's algorithm to find shortest paths from a start node
    public void dijkstra(String startNode) {
        if (!adjList.containsKey(startNode)) {
            System.out.println("Start node " + startNode + " does not exist in the graph.");
            return;
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previousNodes = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialize distances
        for (String node : adjList.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
            previousNodes.put(node, null);
        }

        distances.put(startNode, 0);
        pq.add(startNode);

        while (!pq.isEmpty()) {
            String currentNode = pq.poll();

            // If the shortest distance is infinity, the remaining nodes are unreachable
            if (distances.get(currentNode) == Integer.MAX_VALUE) {
                break;
            }

            List<Edge> neighbors = adjList.get(currentNode);
            if (neighbors == null) continue;

            for (Edge edge : neighbors) {
                String neighbor = edge.destination;
                int newDist = distances.get(currentNode) + edge.weight;

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, currentNode);
                    
                    // PriorityQueue doesn't have an update mechanism, 
                    // so we remove and re-add to update priority.
                    pq.remove(neighbor);
                    pq.add(neighbor);
                }
            }
        }

        printDijkstraResult(startNode, distances, previousNodes);
    }

    // Helper to print exact paths and distances
    private void printDijkstraResult(String startNode, Map<String, Integer> distances, Map<String, String> previousNodes) {
        System.out.println("Shortest paths from start node: " + startNode);
        for (String node : distances.keySet()) {
            if (node.equals(startNode)) continue;

            System.out.print("To " + node + " - Distance: ");
            if (distances.get(node) == Integer.MAX_VALUE) {
                System.out.println("Infinity (Unreachable)");
            } else {
                System.out.print(distances.get(node) + ", Path: ");
                printPath(node, previousNodes);
                System.out.println();
            }
        }
    }

    private void printPath(String targetNode, Map<String, String> previousNodes) {
        List<String> path = new ArrayList<>();
        String current = targetNode;
        while (current != null) {
            path.add(current);
            current = previousNodes.get(current);
        }
        Collections.reverse(path);
        System.out.print(String.join(" -> ", path));
    }
    
    // For testing purposes
    public Map<String, List<Edge>> getAdjList() {
        return adjList;
    }
}
