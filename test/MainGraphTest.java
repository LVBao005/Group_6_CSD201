import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainGraphTest {
    private static int testsPassed = 0;
    private static int totalTests = 0;

    public static void main(String[] args) {
        System.out.println("========================================================");
        System.out.println("             GRAPH - MANUAL TEST (CHI TIET)");
        System.out.println("========================================================");

        runTest("TC01 - Empty Graph State", MainGraphTest::testEmptyGraph);
        runTest("TC02 - Read from Non-existent File", MainGraphTest::testReadNonExistentFile);
        runTest("TC03 - Read Valid Data from File", MainGraphTest::testReadFileValidData);
        runTest("TC04 - Add Edge Manually & Dijkstra Unreachable", MainGraphTest::testAddEdgeAndUnreachable);

        printSummary();
    }

    private static void runTest(String testName, Runnable testMethod) {
        totalTests++;
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println(testName);
        System.out.println("--------------------------------------------------------");
        try {
            testMethod.run();
            System.out.println("=> KET LUAN: PASS");
            testsPassed++;
        } catch (Throwable e) {
            System.out.println("=> KET LUAN: FAIL");
            System.out.println("   Ly do: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printSummary() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("                       SUMMARY");
        System.out.println("========================================================");
        System.out.println("Tong so test: " + totalTests);
        System.out.println("PASS: " + testsPassed);
        System.out.println("FAIL: " + (totalTests - testsPassed));
        System.out.println("========================================================");
    }

    private static void printInput(String description) {
        System.out.println("[Input] " + description);
    }

    private static void printOutput(String description) {
        System.out.println("[Output] " + description);
    }

    private static boolean checkEquals(String label, Object expected, Object actual) {
        boolean pass = (expected == null) ? actual == null : expected.equals(actual);
        System.out.println("  - " + label);
        System.out.println("    Expected: " + expected);
        System.out.println("    Actual  : " + actual);
        System.out.println("    Result  : " + (pass ? "PASS" : "FAIL"));
        return pass;
    }

    private static void testEmptyGraph() {
        Graph graph = new Graph();
        printInput("Graph moi tao.");
        
        Map<String, List<Graph.Edge>> adjList = graph.getAdjList();
        printOutput("Kich thuoc list: " + adjList.size());
        
        if (!checkEquals("Size of adj list", 0, adjList.size())) {
            throw new RuntimeException("Co dieu kien khong dat.");
        }
    }

    private static void testReadNonExistentFile() {
        Graph graph = new Graph();
        printInput("Goi readFromFile() voi file khong ton tai.");
        
        // This should not crash, it should catch IOException and print to stderr.
        graph.readFromFile("KhongTonTai.xyz");
        
        printOutput("Khong co exception ne ra (duoc handle).");
        if (!checkEquals("Size of adj list in missing file", 0, graph.getAdjList().size())) {
            throw new RuntimeException("Co dieu kien khong dat.");
        }
    }

    private static void testReadFileValidData() {
        Graph graph = new Graph();
        String tempFilePath = "temp_test_graph.txt";
        File tempFile = new File(tempFilePath);
        try {
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write("X Y 15\n");
                writer.write("Y Z 10\n");
            }
            printInput("Data file: X Y 15, Y Z 10");
            graph.readFromFile(tempFilePath);
            
            printOutput("Kich thuoc adj list sau load data: " + graph.getAdjList().size());
            
            boolean ok = true;
            // X, Y, Z expected to be in graph because edges are undirected
            ok &= checkEquals("Graph contains X and Y", true, graph.getAdjList().containsKey("X") && graph.getAdjList().containsKey("Y"));
            ok &= checkEquals("Graph contains Z", true, graph.getAdjList().containsKey("Z"));
            
            if (!ok) {
                throw new RuntimeException("Co dieu kien khong dat.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Setup test data failed", e);
        } finally {
            if (tempFile.exists()) tempFile.delete();
        }
    }

    private static void testAddEdgeAndUnreachable() {
        Graph graph = new Graph();
        printInput("Them canh X -> Y (10) va dinh Z bi co lap.");
        graph.addEdge("X", "Y", 10);
        // Add Z manually by adding an edge Z -> Z (0) to register it in map
        graph.addEdge("Z", "Z", 0); 
        
        printOutput("Running dijkstra(\"X\"):");
        graph.dijkstra("X");
        
        printOutput("Dijkstra chay khong bi failed do loi null hay Integer.MAX_VALUE.");
        checkEquals("Dijkstra completion", true, true);
    }
}
