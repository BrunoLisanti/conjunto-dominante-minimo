package tests;

import negocio.Graph;
import org.junit.Before;
import org.junit.Test;
import presentacion.GraphPresenter;
import presentacion.GraphView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GraphPresenterTests {

    private FakeView view;
    private GraphPresenter presenter;

    @Before
    public void setUp() {
        view = new FakeView();
        presenter = new GraphPresenter(view);
        presenter.start();
    }

    // ---------- Vértices y aristas ----------

    @Test
    public void startsWithEmptyGraphTest() {
        assertEquals(0, view.shownGraph.getSize());
    }

    @Test
    public void addVertexShowsBiggerGraphTest() {
        addVertices(2);
        assertEquals(2, view.shownGraph.getSize());
    }

    @Test
    public void addEdgeShowsEdgeTest() {
        addVertices(2);
        presenter.addEdge(0, 1);
        assertTrue(view.shownGraph.edgeExists(0, 1));
    }

    @Test
    public void addValidEdgeShowsNoErrorTest() {
        addVertices(2);
        presenter.addEdge(0, 1);
        assertNull(view.lastError);
    }

    @Test
    public void addEdgeWithSameVertexShowsErrorTest() {
        addVertices(2);
        presenter.addEdge(1, 1);
        assertNotNull(view.lastError);
    }

    @Test
    public void addRepeatedEdgeShowsErrorTest() {
        addVertices(2);
        presenter.addEdge(0, 1);
        presenter.addEdge(1, 0);
        assertNotNull(view.lastError);
    }

    @Test
    public void addRepeatedEdgeDoesNotDuplicateTest() {
        addVertices(2);
        presenter.addEdge(0, 1);
        presenter.addEdge(1, 0);
        assertEquals(1, view.shownGraph.getEdgeCount());
    }

    @Test
    public void newGraphEmptiesGraphTest() {
        addVertices(3);
        presenter.newGraph();
        assertEquals(0, view.shownGraph.getSize());
    }

    // ---------- Cálculo ----------

    @Test
    public void calculateShowsDominatingSetTest() {
        addVertices(3);
        presenter.addEdge(0, 1);
        presenter.addEdge(0, 2);
        presenter.calculateDominatingSet();
        assertEquals(Set.of(0), view.shownDominatingSet);
    }

    @Test
    public void calculateOnEmptyGraphShowsErrorTest() {
        presenter.calculateDominatingSet();
        assertNotNull(view.lastError);
    }

    @Test
    public void calculateOnEmptyGraphShowsNoResultTest() {
        presenter.calculateDominatingSet();
        assertNull(view.shownDominatingSet);
    }

    @Test
    public void changingGraphClearsResultTest() {
        addVertices(2);
        presenter.calculateDominatingSet();
        presenter.addEdge(0, 1);
        assertNull(view.shownDominatingSet);
    }

    // ---------- Carga desde archivo ----------

    @Test
    public void loadValidFileShowsSizeTest() throws IOException {
        presenter.loadFromFile(temporaryFile("3\n0 1\n1 2\n"));
        assertEquals(3, view.shownGraph.getSize());
    }

    @Test
    public void loadValidFileShowsEdgesTest() throws IOException {
        presenter.loadFromFile(temporaryFile("3\n0 1\n1 2\n"));
        assertTrue(view.shownGraph.edgeExists(1, 2));
    }

    @Test
    public void loadValidFileShowsNoErrorTest() throws IOException {
        presenter.loadFromFile(temporaryFile("3\n0 1\n1 2\n"));
        assertNull(view.lastError);
    }

    @Test
    public void loadInvalidFileShowsErrorTest() throws IOException {
        presenter.loadFromFile(temporaryFile("3\n0 9\n"));
        assertNotNull(view.lastError);
    }

    @Test
    public void loadInvalidFileKeepsPreviousGraphTest() throws IOException {
        addVertices(2);
        presenter.loadFromFile(temporaryFile("3\n0 9\n"));
        assertEquals(2, view.shownGraph.getSize());
    }

    @Test
    public void loadMissingFileShowsErrorTest() {
        presenter.loadFromFile(Path.of("no-existe-este-archivo.txt"));
        assertNotNull(view.lastError);
    }

    // ---------- Auxiliares ----------

    private void addVertices(int count) {
        for (int i = 0; i < count; i++) {
            presenter.addVertex();
        }
    }

    private static Path temporaryFile(String content) throws IOException {
        Path file = Files.createTempFile("grafo", ".txt");
        Files.writeString(file, content, StandardCharsets.UTF_8);
        file.toFile().deleteOnExit();
        return file;
    }

    /** Vista falsa que solo guarda lo último que le pidieron mostrar. */
    private static class FakeView implements GraphView {
        Graph shownGraph;
        Set<Integer> shownDominatingSet;
        String lastError;

        @Override
        public void showGraph(Graph graph) {
            shownGraph = graph;
        }

        @Override
        public void showDominatingSet(Set<Integer> dominatingSet) {
            shownDominatingSet = dominatingSet;
        }

        @Override
        public void clearDominatingSet() {
            shownDominatingSet = null;
        }

        @Override
        public void showError(String message) {
            lastError = message;
        }
    }
}
