package negocio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Lee un grafo desde un archivo de texto plano con este formato:
 *
 * <pre>
 * # Las líneas que empiezan con # son comentarios
 * 6          &lt;- primera línea: cantidad de vértices
 * 0 1        &lt;- cada línea siguiente: una arista entre dos vértices
 * 0 4
 * </pre>
 *
 * Los vértices se numeran desde 0. Las líneas vacías se ignoran.
 */
public class GraphFileReader {

    public static Graph read(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return read(reader);
        }
    }

    public static Graph read(Reader reader) throws IOException {
        BufferedReader buffered = reader instanceof BufferedReader
                ? (BufferedReader) reader
                : new BufferedReader(reader);

        Graph graph = null;
        String line;
        int lineNumber = 0;

        while ((line = buffered.readLine()) != null) {
            lineNumber++;
            line = removeComment(line).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");

            if (graph == null) {
                if (parts.length != 1)
                    throw formatError(lineNumber, "se esperaba la cantidad de vértices");
                int size = parseNumber(parts[0], lineNumber);
                if (size < 0)
                    throw formatError(lineNumber, "la cantidad de vértices no puede ser negativa");
                graph = new Graph(size);
            } else {
                if (parts.length != 2)
                    throw formatError(lineNumber, "se esperaba una arista con dos vértices, por ejemplo \"0 1\"");
                int vertex1 = parseNumber(parts[0], lineNumber);
                int vertex2 = parseNumber(parts[1], lineNumber);
                try {
                    graph.addEdge(vertex1, vertex2);
                } catch (IllegalArgumentException e) {
                    throw formatError(lineNumber, e.getMessage());
                }
            }
        }

        if (graph == null)
            throw new IllegalArgumentException("El archivo está vacío");

        return graph;
    }

    private static String removeComment(String line) {
        int index = line.indexOf('#');
        return index >= 0 ? line.substring(0, index) : line;
    }

    private static int parseNumber(String text, int lineNumber) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw formatError(lineNumber, "\"" + text + "\" no es un número");
        }
    }

    private static IllegalArgumentException formatError(int lineNumber, String message) {
        return new IllegalArgumentException("Línea " + lineNumber + ": " + message);
    }
}
