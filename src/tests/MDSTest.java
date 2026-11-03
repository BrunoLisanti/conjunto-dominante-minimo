package tests;

import negocio.Graph;
import negocio.MinimumDominatingSet;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MDSTest {
    @Test
    public void figura1Test() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 4);
        graph.addEdge(1, 4);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);

        Set<Integer> mds = MinimumDominatingSet.obtain(graph);

        assertEquals(2, mds.size());
    }

    @Test
    public void starGraphTest() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(0, 4);

        Set<Integer> mds = MinimumDominatingSet.obtain(graph);

        Set<Integer> esperado = Set.of(0);

        assertEquals(esperado, mds);
    }

    @Test
    public void disconnectedGraphTest() {
        Graph graph = new Graph(4);

        Set<Integer> mds = MinimumDominatingSet.obtain(graph);

        Set<Integer> esperado = Set.of(0, 1, 2, 3);

        // Están aislados, el conjunto dominante es tomar todos los vértices
        assertEquals(esperado, mds);
    }

    @Test
    public void everythingConnectedGraphTest() {
        Graph graph = new Graph(4);

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);

        Set<Integer> mds = MinimumDominatingSet.obtain(graph);

        // Solo es necesario un vértice, cualquiera
        assertEquals(1, mds.size());
    }
}
