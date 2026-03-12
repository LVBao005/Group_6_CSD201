public class MainGraph {
    public static void main(String[] args) {
        System.out.println("========================================================");
        System.out.println("            MINI MAP ROUTING SYSTEM (DIJKSTRA)");
        System.out.println("========================================================");

        Graph graph = new Graph();
        String filepath = "graph.txt";
        
        System.out.println("Loading graph data from: " + filepath);
        graph.readFromFile(filepath);
        
        System.out.println("Loaded Graph Adjacency List:");
        graph.getAdjList().forEach((source, edges) -> {
            System.out.print(source + " -> ");
            for (Graph.Edge e : edges) {
                System.out.print("[" + e.destination + ", " + e.weight + "] ");
            }
            System.out.println();
        });
        
        System.out.println("\n--------------------------------------------------------");
        System.out.println("Running Dijkstra's Algorithm...");
        System.out.println("--------------------------------------------------------");
        // We will start from 'A' as it's the beginning of our sample graph
        graph.dijkstra("A");

        System.out.println("========================================================");
    }
}
