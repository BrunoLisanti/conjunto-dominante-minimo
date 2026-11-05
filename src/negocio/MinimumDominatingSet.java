package negocio;

import java.util.HashSet;
import java.util.Set;

public class MinimumDominatingSet {

    public static Set<Integer> obtain(Graph graph) {
        Set<Integer> mds = new HashSet<>();
        Set<Integer> covered = new HashSet<>();
        while (covered.size() != graph.getSize()) {
            int maxCoverVertex = 0;
            int maxCoverage = 0;
            for (int i = 0; i < graph.getSize(); i++) {

                int coverage = calculateCoverage(i, graph, covered);
                if (coverage > maxCoverage) {
                    maxCoverage = coverage;
                    maxCoverVertex = i;
                }
            }
            covered.add(maxCoverVertex);
            covered.addAll(graph.getNeighbours(maxCoverVertex));
            mds.add(maxCoverVertex);
        }

        return mds;
    }

    // Dada la covertura actual y dado un vértice, vemos cuánto cubre dicho vértice que no esté cubierto ya.
    private static int calculateCoverage(int vertex, Graph graph, Set<Integer> covered) {
        Set<Integer> coverage = new HashSet<>(graph.getNeighbours(vertex));
        coverage.add(vertex);
        coverage.removeAll(covered);
        return coverage.size();
    }
}
