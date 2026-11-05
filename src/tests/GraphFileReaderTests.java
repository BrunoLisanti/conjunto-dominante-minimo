package tests;

import negocio.Graph;
import negocio.GraphFileReader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphFileReaderTests {

    private static final String VALID_GRAPH = "3\n0 1\n1 2\n";

    // ---------- Archivos válidos ----------

    @Test
    public void readsSizeTest() throws IOException {
        assertEquals(3, read(VALID_GRAPH).getSize());
    }

    @Test
    public void readsFirstEdgeTest() throws IOException {
        assertTrue(read(VALID_GRAPH).edgeExists(0, 1));
    }

    @Test
    public void readsSecondEdgeTest() throws IOException {
        assertTrue(read(VALID_GRAPH).edgeExists(1, 2));
    }

    @Test
    public void doesNotAddMissingEdgeTest() throws IOException {
        assertFalse(read(VALID_GRAPH).edgeExists(0, 2));
    }

    @Test
    public void graphWithoutEdgesSizeTest() throws IOException {
        assertEquals(4, read("4\n").getSize());
    }

    @Test
    public void graphWithoutEdgesHasNoEdgesTest() throws IOException {
        assertEquals(0, read("4\n").getEdgeCount());
    }

    @Test
    public void ignoresCommentsAndEmptyLinesTest() throws IOException {
        Graph graph = read("# grafo de prueba\n\n2\n\n0 1   # arista\n");
        assertTrue(graph.edgeExists(0, 1));
    }

    @Test
    public void acceptsExtraSpacesTest() throws IOException {
        assertTrue(read("  3  \n 0    2 \n").edgeExists(0, 2));
    }

    // ---------- Archivos inválidos ----------

    @Test(expected = IllegalArgumentException.class)
    public void emptyFileTest() throws IOException {
        read("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void onlyCommentsFileTest() throws IOException {
        read("# nada\n# por aca\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void sizeIsNotANumberTest() throws IOException {
        read("abc\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeSizeTest() throws IOException {
        read("-3\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void edgeWithOneVertexTest() throws IOException {
        read("3\n0\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void edgeWithNonExistingVertexTest() throws IOException {
        read("3\n0 7\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void loopEdgeTest() throws IOException {
        read("3\n1 1\n");
    }

    @Test
    public void errorMessageIncludesLineNumberTest() throws IOException {
        assertTrue(errorMessageOf("3\n0 1\n0 x\n").startsWith("Línea 3"));
    }

    // ---------- Auxiliares ----------

    private static Graph read(String content) throws IOException {
        return GraphFileReader.read(new StringReader(content));
    }

    // Devuelve el mensaje de la excepción, o "" si no hubo error.
    private static String errorMessageOf(String content) throws IOException {
        try {
            read(content);
            return "";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
