package negocio;

import java.util.HashSet;
import java.util.Set;

public class MinimumDominatingSet {

    public static Set<Integer> obtain(Graph graph) {
        if (graph == null)
            throw new IllegalArgumentException("El grafo no puede ser null");

        Set<Integer> mds = new HashSet<>();
        Set<Integer> covered = new HashSet<>();
        while (covered.size() != graph.getSize()) {
            int vertex = mostCoveringVertex(graph, covered);
            covered.add(vertex);
            covered.addAll(graph.getNeighbours(vertex));
            mds.add(vertex);
        }
        return mds;
    }

    // Elección golosa: el vértice que cubre más vértices todavía no cubiertos.
    private static int mostCoveringVertex(Graph graph, Set<Integer> covered) {
        int bestVertex = 0;
        int bestCoverage = 0;
        for (int vertex = 0; vertex < graph.getSize(); vertex++) {
            int coverage = calculateCoverage(vertex, graph, covered);
            if (coverage > bestCoverage) {
                bestCoverage = coverage;
                bestVertex = vertex;
            }
        }
        return bestVertex;
    }

    private static int calculateCoverage(int vertex, Graph graph, Set<Integer> covered) {
        Set<Integer> coverage = new HashSet<>(graph.getNeighbours(vertex));
        coverage.add(vertex);
        coverage.removeAll(covered);
        return coverage.size();
    }
}
