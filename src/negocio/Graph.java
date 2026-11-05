package negocio;

import java.util.HashSet;
import java.util.Set;

/**
 * Grafo simple no dirigido representado con una matriz de adyacencia.
 * Los vértices se identifican con enteros de 0 a getSize() - 1.
 */
public class Graph {
    private boolean[][] adjacencyMatrix;

    public Graph(int vertexCount) {
        if (vertexCount < 0)
            throw new IllegalArgumentException("La cantidad de vértices no puede ser negativa: " + vertexCount);

        adjacencyMatrix = new boolean[vertexCount][vertexCount];
    }

    /**
     * Agrega un vértice nuevo sin aristas y devuelve su número.
     * Copia la matriz a una de tamaño (n+1) x (n+1), por lo que es O(n²).
     */
    public int addVertex() {
        int size = getSize();
        boolean[][] newMatrix = new boolean[size + 1][size + 1];
        for (int i = 0; i < size; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, size);
        }
        adjacencyMatrix = newMatrix;
        return size;
    }

    public void addEdge(int vertex1, int vertex2) {
        validateEdge(vertex1, vertex2);
        adjacencyMatrix[vertex1][vertex2] = true;
        adjacencyMatrix[vertex2][vertex1] = true;
    }

    public void removeEdge(int vertex1, int vertex2) {
        validateEdge(vertex1, vertex2);
        adjacencyMatrix[vertex1][vertex2] = false;
        adjacencyMatrix[vertex2][vertex1] = false;
    }

    public boolean edgeExists(int vertex1, int vertex2) {
        validateEdge(vertex1, vertex2);
        return adjacencyMatrix[vertex1][vertex2];
    }

    public Set<Integer> getNeighbours(int vertex) {
        validateVertex(vertex);
        Set<Integer> neighbours = new HashSet<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[vertex][i]) {
                neighbours.add(i);
            }
        }
        return neighbours;
    }

    public int getSize() {
        return adjacencyMatrix.length;
    }

    public int getEdgeCount() {
        int count = 0;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = i + 1; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j]) count++;
            }
        }
        return count;
    }

    public boolean compare(Graph graph) {
        if (this.getSize() != graph.getSize())
            return false;

        int vertices = adjacencyMatrix.length;
        for (int i = 0; i < vertices; i++) {
            for (int j = i + 1; j < vertices; j++) {
                if (adjacencyMatrix[i][j] != graph.edgeExists(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= getSize())
            throw new IllegalArgumentException("El vértice " + vertex + " no existe");
    }

    private void validateEdge(int vertex1, int vertex2) {
        validateVertex(vertex1);
        validateVertex(vertex2);
        if (vertex1 == vertex2)
            throw new IllegalArgumentException("No se permiten aristas de un vértice a sí mismo (" + vertex1 + ")");
    }
}
