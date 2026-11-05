package presentacion;

import negocio.Graph;
import negocio.GraphFileReader;
import negocio.MinimumDominatingSet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/**
 * Presentador (patrón MVP): recibe las acciones del usuario desde la vista,
 * las aplica sobre el grafo (modelo) y le indica a la vista qué mostrar.
 * No depende de Swing, por eso se puede testear con una vista falsa.
 */
public class GraphPresenter {

    private final GraphView view;
    private Graph graph;

    public GraphPresenter(GraphView view) {
        this.view = view;
        this.graph = new Graph(0);
    }

    public void start() {
        graphChanged();
    }

    public void addVertex() {
        graph.addVertex();
        graphChanged();
    }

    public void addEdge(int vertex1, int vertex2) {
        if (vertex1 == vertex2) {
            view.showError("Elegí dos vértices distintos.");
            return;
        }
        if (graph.edgeExists(vertex1, vertex2)) {
            view.showError("La arista " + vertex1 + " - " + vertex2 + " ya existe.");
            return;
        }
        graph.addEdge(vertex1, vertex2);
        graphChanged();
    }

    public void loadFromFile(Path path) {
        try {
            graph = GraphFileReader.read(path);
            graphChanged();
        } catch (IOException e) {
            view.showError("No se pudo leer el archivo: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            view.showError("El archivo no tiene el formato esperado.\n" + e.getMessage());
        }
    }

    public void newGraph() {
        graph = new Graph(0);
        graphChanged();
    }

    public void calculateDominatingSet() {
        if (graph.getSize() == 0) {
            view.showError("El grafo no tiene vértices.");
            return;
        }
        Set<Integer> dominatingSet = MinimumDominatingSet.obtain(graph);
        view.showDominatingSet(dominatingSet);
    }

    // Cualquier cambio en el grafo invalida el resultado calculado antes.
    private void graphChanged() {
        view.showGraph(graph);
        view.clearDominatingSet();
    }
}
