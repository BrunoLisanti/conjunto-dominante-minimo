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
        @Test
    public void emptyGraphTest() {
        Graph graph = new Graph(0);
 
        assertTrue(MinimumDominatingSet.obtain(graph).isEmpty());
    }
 
    @Test
    public void singleVertexTest() {
        Graph graph = new Graph(1);
 
        assertEquals(Set.of(0), MinimumDominatingSet.obtain(graph));
    }
 
    @Test
    public void pathGraphTest() {
        // 0 - 1 - 2 - 3 - 4
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
 
        Set<Integer> mds = MinimumDominatingSet.obtain(graph);
 
        assertEquals(2, mds.size());
        assertTrue(isDominating(graph, mds));
    }
 
    @Test
    public void cycleGraphTest() {
        // Ciclo de 6 vértices: el mínimo es 2
        Graph graph = new Graph(6);
        for (int i = 0; i < 6; i++) {
            graph.addEdge(i, (i + 1) % 6);
        }
 
        Set<Integer> mds = MinimumDominatingSet.obtain(graph);
 
        assertEquals(2, mds.size());
        assertTrue(isDominating(graph, mds));
    }
 
    @Test
    public void starWithIsolatedVertexTest() {
        // Estrella con centro 0 y un vértice aislado (4) que debe estar sí o sí
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
 
        assertEquals(Set.of(0, 4), MinimumDominatingSet.obtain(graph));
    }
 
    @Test
    public void twoComponentsTest() {
        // Dos aristas sueltas: 0-1 y 2-3, hace falta un vértice de cada una
        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(2, 3);
 
        Set<Integer> mds = MinimumDominatingSet.obtain(graph);
 
        assertEquals(2, mds.size());
        assertTrue(isDominating(graph, mds));
    }
 

    
 
    private static boolean isDominating(Graph graph, Set<Integer> set) {
        for (int v = 0; v < graph.getSize(); v++) {
            if (set.contains(v)) continue;
 
            boolean hasNeighbourInSet = false;
            for (int u : graph.getNeighbours(v)) {
                if (set.contains(u)) {
                    hasNeighbourInSet = true;
                    break;
                }
            }
            if (!hasNeighbourInSet) return false;
        }
        return true;
    }
 

}
    
}
