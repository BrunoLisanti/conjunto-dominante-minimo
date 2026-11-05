package interfaz;

import negocio.Graph;
import presentacion.GraphPresenter;
import presentacion.GraphView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * Ventana principal. Solo arma los componentes Swing y le pasa las acciones
 * del usuario al presentador; no contiene lógica del problema.
 */
public class MainForm implements GraphView {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 650;

    private JFrame frame;
    private GraphPanel graphPanel;
    private JComboBox<Integer> firstVertexCombo;
    private JComboBox<Integer> secondVertexCombo;
    private JButton addEdgeButton;
    private JLabel statusLabel;
    private JTextArea resultArea;

    private final GraphPresenter presenter;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainForm window = new MainForm();
            window.frame.setVisible(true);
        });
    }

    public MainForm() {
        initialize();
        presenter = new GraphPresenter(this);
        presenter.start();
    }

    private void initialize() {
        frame = new JFrame("Conjunto dominante mínimo");
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        graphPanel = new GraphPanel();
        frame.getContentPane().add(graphPanel, BorderLayout.CENTER);
        frame.getContentPane().add(createSidePanel(), BorderLayout.EAST);
        frame.getContentPane().add(createStatusBar(), BorderLayout.SOUTH);
    }

    // ---------- Armado de la ventana ----------

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        sidePanel.setPreferredSize(new Dimension(290, HEIGHT));

        sidePanel.add(createGraphSection());
        sidePanel.add(Box.createVerticalStrut(12));
        sidePanel.add(createEdgeSection());
        sidePanel.add(Box.createVerticalStrut(12));
        sidePanel.add(createResultSection());
        sidePanel.add(Box.createVerticalGlue());
        return sidePanel;
    }

    private JPanel createGraphSection() {
        JPanel section = createSection("Grafo", 3);
        section.add(createButton("Agregar vértice", () -> presenter.addVertex()));
        section.add(createButton("Cargar desde archivo...", this::chooseFileToLoad));
        section.add(createButton("Nuevo grafo", this::confirmNewGraph));
        return section;
    }

    private JPanel createEdgeSection() {
        JPanel section = createSection("Agregar arista", 3);
        firstVertexCombo = new JComboBox<>();
        secondVertexCombo = new JComboBox<>();
        addEdgeButton = createButton("Agregar arista", this::addSelectedEdge);

        section.add(labeled("Desde el vértice:", firstVertexCombo));
        section.add(labeled("Hasta el vértice:", secondVertexCombo));
        section.add(addEdgeButton);
        return section;
    }

    private JPanel createResultSection() {
        JPanel section = new JPanel(new BorderLayout(0, 8));
        section.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Conjunto dominante"), new EmptyBorder(4, 6, 6, 6)));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        resultArea = new JTextArea(6, 20);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(resultArea.getFont().deriveFont(14f));

        section.add(createButton("Calcular (algoritmo goloso)", () -> presenter.calculateDominatingSet()), BorderLayout.NORTH);
        section.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        return section;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        statusLabel = new JLabel();
        statusBar.add(statusLabel);
        return statusBar;
    }

    private static JPanel createSection(String title, int rows) {
        JPanel section = new JPanel(new GridLayout(rows, 1, 0, 8));
        section.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(title), new EmptyBorder(4, 6, 6, 6)));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, rows * 42 + 30));
        return section;
    }

    private static JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        return button;
    }

    private static JPanel labeled(String text, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.add(new JLabel(text), BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);
        return row;
    }

    // ---------- Acciones del usuario ----------

    private void addSelectedEdge() {
        Integer first = (Integer) firstVertexCombo.getSelectedItem();
        Integer second = (Integer) secondVertexCombo.getSelectedItem();
        presenter.addEdge(first, second);
    }

    private void chooseFileToLoad() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Elegí el archivo del grafo");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt"));
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            presenter.loadFromFile(chooser.getSelectedFile().toPath());
        }
    }

    private void confirmNewGraph() {
        int answer = JOptionPane.showConfirmDialog(frame,
                "Se va a borrar el grafo actual. ¿Continuar?",
                "Nuevo grafo", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            presenter.newGraph();
        }
    }

    // ---------- GraphView: lo que el presentador le pide mostrar ----------

    @Override
    public void showGraph(Graph graph) {
        graphPanel.setGraph(graph);
        updateVertexCombos(graph.getSize());
        statusLabel.setText("Vértices: " + graph.getSize() + "    Aristas: " + graph.getEdgeCount());
    }

    @Override
    public void showDominatingSet(Set<Integer> dominatingSet) {
        graphPanel.setDominatingSet(dominatingSet);
        resultArea.setText("Tamaño: " + dominatingSet.size()
                + "\nVértices: " + asSetNotation(dominatingSet));
    }

    private static String asSetNotation(Set<Integer> vertices) {
        String sorted = new TreeSet<>(vertices).toString();
        return "{" + sorted.substring(1, sorted.length() - 1) + "}";
    }

    @Override
    public void clearDominatingSet() {
        graphPanel.clearDominatingSet();
        resultArea.setText("Todavía no se calculó.");
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    private void updateVertexCombos(int vertexCount) {
        firstVertexCombo.removeAllItems();
        secondVertexCombo.removeAllItems();
        for (int vertex = 0; vertex < vertexCount; vertex++) {
            firstVertexCombo.addItem(vertex);
            secondVertexCombo.addItem(vertex);
        }
        if (vertexCount > 1) secondVertexCombo.setSelectedIndex(1);
        addEdgeButton.setEnabled(vertexCount >= 2);
    }
}
