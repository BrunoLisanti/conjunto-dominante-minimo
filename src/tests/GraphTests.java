package tests;

import negocio.Graph;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphTests {
    @Test()
    public void addEdgeTest() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1);
        assertTrue(graph.edgeExists(0, 1));
    }

    @Test()
    public void removeEdgeTest() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1);
        graph.removeEdge(0, 1);
        assertFalse(graph.edgeExists(0, 1));
    }
}
