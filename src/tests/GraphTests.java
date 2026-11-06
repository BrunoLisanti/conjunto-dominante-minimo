package tests;

import negocio.Graph;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphTests {

    // ---------- Aristas ----------

    @Test
    public void addEdgeTest() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1);
        assertTrue(graph.edgeExists(0, 1));
    }

    @Test
    public void removeEdgeTest() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1);
        graph.removeEdge(0, 1);
        assertFalse(graph.edgeExists(0, 1));
    }

    @Test
    public void edgeIsSymmetricTest() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 2);
        assertTrue(graph.edgeExists(2, 0));
    }

    @Test
    public void addSameEdgeTwiceTest() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1);
        graph.addEdge(0, 1);
        assertEquals(Set.of(1), graph.getNeighbours(0));
    }

    @Test
    public void removeNonExistingEdgeTest() {
        Graph graph = new Graph(2);
        graph.removeEdge(0, 1);
        assertFalse(graph.edgeExists(0, 1));
    }

    @Test
    public void getEdgeCountTest() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 1); // repetida, no cuenta dos veces
        assertEquals(2, graph.getEdgeCount());
    }

    // ---------- Vecinos y tamaño ----------

    @Test
    public void isolatedVertexHasNoNeighboursTest() {
        Graph graph = new Graph(3);
        assertTrue(graph.getNeighbours(1).isEmpty());
    }

    @Test
    public void starCenterNeighboursTest() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        assertEquals(Set.of(1, 2, 3), graph.getNeighbours(0));
    }

    @Test
    public void getSizeTest() {
        assertEquals(7, new Graph(7).getSize());
    }

    // ---------- Agregar vértices ----------

    @Test
    public void addVertexReturnsNewVertexNumberTest() {
        Graph graph = new Graph(2);
        assertEquals(2, graph.addVertex());
    }

    @Test
    public void addVertexIncreasesSizeTest() {
        Graph graph = new Graph(2);
        graph.addVertex();
        assertEquals(3, graph.getSize());
    }

    @Test
    public void addVertexToEmptyGraphReturnsZeroTest() {
        Graph graph = new Graph(0);
        assertEquals(0, graph.addVertex());
    }

    @Test
    public void addVertexToEmptyGraphIncreasesSizeTest() {
        Graph graph = new Graph(0);
        graph.addVertex();
        assertEquals(1, graph.getSize());
    }

    @Test
    public void addVertexKeepsEdgesTest() {
        Graph graph = new Graph(3);
        graph.addEdge(1, 2);
        graph.addVertex();
        assertTrue(graph.edgeExists(1, 2));
    }

    @Test
    public void addedVertexHasNoNeighboursTest() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1);
        graph.addVertex();
        assertTrue(graph.getNeighbours(3).isEmpty());
    }

    // ---------- Comparación ----------

    @Test
    public void compareEqualGraphsTest() {
        Graph g1 = new Graph(3);
        Graph g2 = new Graph(3);
        g1.addEdge(0, 1);
        g2.addEdge(1, 0);
        assertTrue(g1.hasSameEdgesAs(g2));
    }

    @Test
    public void compareDifferentSizeTest() {
        assertFalse(new Graph(3).hasSameEdgesAs(new Graph(4)));
    }

    @Test
    public void compareDifferentEdgesTest() {
        Graph g1 = new Graph(3);
        Graph g2 = new Graph(3);
        g1.addEdge(0, 1);
        g2.addEdge(1, 2);
        assertFalse(g1.hasSameEdgesAs(g2));
    }

    // ---------- Validaciones ----------

    @Test(expected = IllegalArgumentException.class)
    public void negativeSizeTest() {
        new Graph(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEdgeNonExistingVertexTest() {
        new Graph(3).addEdge(0, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEdgeNegativeVertexTest() {
        new Graph(3).addEdge(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addLoopTest() {
        new Graph(3).addEdge(1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void neighboursOfNonExistingVertexTest() {
        new Graph(3).getNeighbours(3);
    }
}
