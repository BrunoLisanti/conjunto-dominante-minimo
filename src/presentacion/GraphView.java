package presentacion;

import negocio.Graph;

import java.util.Set;

/**
 * Lo que el presentador necesita de una vista.
 * La interfaz Swing la implementa, y los tests usan una versión falsa.
 */
public interface GraphView {

    void showGraph(Graph graph);

    void showDominatingSet(Set<Integer> dominatingSet);

    void clearDominatingSet();

    void showError(String message);
}
