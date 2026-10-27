package negocio;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Graph {
    boolean[][] _adjMatrix;
    public Graph(int vertexCount) {
        _adjMatrix = new boolean[vertexCount][vertexCount];
    }

    public void addEdge(int vertex1, int vertex2) {
        _adjMatrix[vertex1][vertex2] = true;
        _adjMatrix[vertex2][vertex1] = true;
    }

    public void removeEdge(int vertex1, int vertex2) {
        _adjMatrix[vertex1][vertex2] = false;
        _adjMatrix[vertex2][vertex1] = false;
    }

    public boolean edgeExists(int vertex1, int vertex2) {
        return _adjMatrix[vertex1][vertex2];
    }

    public Set<Integer> getNeighbours(int vertex) {
        Set<Integer> neighbours = new HashSet<>();
        for (int i = 0; i < _adjMatrix.length; i++) {
            if(_adjMatrix[vertex][i]) {
                neighbours.add(i);
            }
        }
        return neighbours;
    }

    public int getSize() {
        return _adjMatrix.length;
    }

    public boolean compare(Graph graph) {
        if (this.getSize() != graph.getSize())
            return false;

        int vertices = _adjMatrix.length;
        for (int i = 0; i < vertices; i++) {
            for (int j = i; j < vertices; j++) {
                if (_adjMatrix[i][j] != graph.edgeExists(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
}
