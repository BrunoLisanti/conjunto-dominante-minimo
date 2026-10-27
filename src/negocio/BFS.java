package negocio;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class BFS {
    public static Set<Integer> reachable(Graph graph, int origin) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new ArrayDeque<Integer>();
        boolean[] marked = new boolean[graph.getSize()];
        queue.add(origin);

        while (!queue.isEmpty()) {
            Integer actualVertex = queue.poll();
            marked[actualVertex] = true;
            visited.add(actualVertex);
            for (Integer vertex : graph.getNeighbours(actualVertex)) {
                if (!marked[vertex]) {
                    marked[vertex] = true;
                    queue.add(vertex);
                }
            }
        }
        return visited;
    }
}
