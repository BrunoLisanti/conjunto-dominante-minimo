package negocio;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MinimumDominatingSet {

    public static Set<Integer> obtain(Graph graph) {
        // Buscamos si hay un un mds de 1, de 2, de 3, hasta n.

        int sizeOfMDS = 1;
        Set<Integer> toTest = new HashSet<>();
        while (true) {
            if (sizeOfMDS == graph.getSize()) {
                return IntStream.rangeClosed(0, graph.getSize() - 1)
                        .boxed()
                        .collect(Collectors.toSet());
            }

            // sizeOfDMS debería representar la cantidad de fors que anidamos.

            return testSetAddedToEachVertex(graph, toTest);

            sizeOfMDS++;
        }
    }

    private static Set<Integer> testSetAddedToEachVertex(Graph graph, Set<Integer> toTest) {
        for (int i = 0; i < graph.getSize(); i++) {
            if (toTest.contains(i))
                continue;

            toTest.add(i);

            if (itsDominatingSetOfGraph(graph, toTest)) {
                return toTest;
            }
            else {
                toTest.remove(i);
            }
        }
    }

    private static Set<Integer> testSetAddedToEachVertexWithSize(Graph graph, Set<Integer> toTest, int MDSsize) {
        // La idea es, itero, si el set tiene el tamaño con el que estamos laburando, entonces lo testeamos, sino, le agregamos un numero.
    }

    private static boolean itsDominatingSetOfGraph(Graph graph, Set<Integer> set) {
        Set<Integer> neighbours = new HashSet<>(set);
        for (Integer v : set) {
            neighbours.addAll(graph.getNeighbours(v));
        }
        return neighbours.size() == graph.getSize();
    }
}
