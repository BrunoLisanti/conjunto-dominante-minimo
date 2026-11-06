package tests;

import negocio.Graph;
import negocio.MinimumDominatingSet;
import org.junit.Test;

import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MDSTest {

    // ---------- Figura 1 del enunciado ----------

    @Test
    public void figura1SizeTest() {
        assertEquals(2, MinimumDominatingSet.obtain(figura1()).size());
    }

    @Test
    public void figura1IsDominatingTest() {
        Graph graph = figura1();
        assertTrue(isDominating(graph, MinimumDominatingSet.obtain(graph)));
    }

    // ---------- Grafos conocidos ----------

    @Test
    public void starGraphTest() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(0, 4);

        assertEquals(Set.of(0), MinimumDominatingSet.obtain(graph));
    }

    @Test
    public void disconnectedGraphTest() {
        Graph graph = new Graph(4);

        // Están aislados, el conjunto dominante es tomar todos los vértices
        assertEquals(Set.of(0, 1, 2, 3), MinimumDominatingSet.obtain(graph));
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

        // Solo es necesario un vértice, cualquiera
        assertEquals(1, MinimumDominatingSet.obtain(graph).size());
    }

    @Test
    public void pathGraphSizeTest() {
        assertEquals(2, MinimumDominatingSet.obtain(path(5)).size());
    }

    @Test
    public void pathGraphIsDominatingTest() {
        Graph graph = path(5);
        assertTrue(isDominating(graph, MinimumDominatingSet.obtain(graph)));
    }

    @Test
    public void cycleGraphSizeTest() {
        assertEquals(2, MinimumDominatingSet.obtain(cycle(6)).size());
    }

    @Test
    public void cycleGraphIsDominatingTest() {
        Graph graph = cycle(6);
        assertTrue(isDominating(graph, MinimumDominatingSet.obtain(graph)));
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
    public void twoComponentsSizeTest() {
        assertEquals(2, MinimumDominatingSet.obtain(twoSeparateEdges()).size());
    }

    @Test
    public void twoComponentsIsDominatingTest() {
        Graph graph = twoSeparateEdges();
        assertTrue(isDominating(graph, MinimumDominatingSet.obtain(graph)));
    }

    // ---------- Casos borde ----------

    @Test
    public void emptyGraphTest() {
        assertTrue(MinimumDominatingSet.obtain(new Graph(0)).isEmpty());
    }

    @Test
    public void singleVertexTest() {
        assertEquals(Set.of(0), MinimumDominatingSet.obtain(new Graph(1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullGraphTest() {
        MinimumDominatingSet.obtain(null);
    }

    @Test
    public void singleEdgeTest() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1);
        assertEquals(1, MinimumDominatingSet.obtain(graph).size());
    }

    @Test
    public void graphIsNotModifiedTest() {
        Graph graph = figura1();
        MinimumDominatingSet.obtain(graph);
        assertTrue(graph.hasSameEdgesAs(figura1()));
    }

    // ---------- Límite del algoritmo goloso ----------

    // El goloso elige primero el vértice 0 (cubre 8) y termina con 3 vértices,
    // pero {1, 2} ya es un conjunto dominante: la heurística no siempre da el mínimo.
    @Test
    public void greedyTrapHasDominatingSetOfSizeTwoTest() {
        assertTrue(isDominating(greedyTrap(), Set.of(1, 2)));
    }

    @Test
    public void greedyIsNotAlwaysOptimalTest() {
        assertEquals(3, MinimumDominatingSet.obtain(greedyTrap()).size());
    }

    // ---------- Grafos aleatorios ----------

    @Test
    public void randomGraphsResultIsDominatingTest() {
        assertTrue(allRandomResultsAreDominating(50, 20, 0.2));
    }

    // ---------- Auxiliares ----------

    private static Graph figura1() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 4);
        graph.addEdge(1, 4);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);
        return graph;
    }

    // 0 - 1 - 2 - ... - (n-1)
    private static Graph path(int n) {
        Graph graph = new Graph(n);
        for (int i = 0; i + 1 < n; i++) {
            graph.addEdge(i, i + 1);
        }
        return graph;
    }

    private static Graph cycle(int n) {
        Graph graph = path(n);
        graph.addEdge(n - 1, 0);
        return graph;
    }

    // Dos aristas sueltas: 0-1 y 2-3
    private static Graph twoSeparateEdges() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(2, 3);
        return graph;
    }

    // 0 es vecino de 1 y de casi todas las hojas; 1 y 2 juntos dominan todo.
    private static Graph greedyTrap() {
        Graph graph = new Graph(11);
        for (int vertex : new int[] {1, 3, 4, 5, 7, 8, 9}) graph.addEdge(0, vertex);
        for (int vertex : new int[] {3, 4, 5, 6}) graph.addEdge(1, vertex);
        for (int vertex : new int[] {7, 8, 9, 10}) graph.addEdge(2, vertex);
        return graph;
    }

    private static boolean allRandomResultsAreDominating(int instances, int size, double density) {
        Random random = new Random(7); // semilla fija: el test da siempre lo mismo
        for (int i = 0; i < instances; i++) {
            Graph graph = randomGraph(size, density, random);
            if (!isDominating(graph, MinimumDominatingSet.obtain(graph))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isDominating(Graph graph, Set<Integer> set) {
        for (int v = 0; v < graph.getSize(); v++) {
            if (!set.contains(v) && !hasNeighbourIn(graph, v, set)) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasNeighbourIn(Graph graph, int vertex, Set<Integer> set) {
        for (int neighbour : graph.getNeighbours(vertex)) {
            if (set.contains(neighbour)) return true;
        }
        return false;
    }

    private static Graph randomGraph(int n, double density, Random random) {
        Graph graph = new Graph(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (random.nextDouble() < density) {
                    graph.addEdge(i, j);
                }
            }
        }
        return graph;
    }
}
