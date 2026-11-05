package tests;

import java.util.Set;
import negocio.BFS;
import negocio.Graph;
import org.junit.Test;

public class BFSTests {

    @Test
    public void reachableConnectedGraphTest() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        assertEquals(Set.of(0, 1, 2, 3), BFS.reachable(graph, 0));
    }

    @Test
    public void reachableDisconnectedGraphTest() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(3, 4);

        assertEquals(Set.of(0, 1), BFS.reachable(graph, 0));
    }

    @Test
    public void reachableIsolatedVertexTest() {
        Graph graph = new Graph(3);

        assertEquals(Set.of(2), BFS.reachable(graph, 2));
    }

    @Test
    public void reachableWithCycleTest() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);

        assertEquals(Set.of(0, 1, 2), BFS.reachable(graph, 1));
    }
}