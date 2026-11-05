package interfaz;

import negocio.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.Set;

/**
 * Dibuja el grafo con los vértices distribuidos en círculo.
 * Los vértices del conjunto dominante se pintan resaltados.
 * Solo se encarga de dibujar: no modifica el grafo ni calcula nada.
 */
public class GraphPanel extends JPanel {

    private static final Color BACKGROUND = new Color(0xF7F8FA);
    private static final Color EDGE = new Color(0xB0B6C0);
    private static final Color VERTEX_FILL = Color.WHITE;
    private static final Color VERTEX_BORDER = new Color(0x4A5260);
    private static final Color DOMINATING_FILL = new Color(0x2F6FDB);
    private static final Color TEXT = new Color(0x2A2F38);
    private static final Color HINT = new Color(0x8A919C);

    private Graph graph;
    private Set<Integer> dominatingSet = new HashSet<>();

    public GraphPanel() {
        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(700, 600));
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        this.dominatingSet = new HashSet<>();
        repaint();
    }

    public void setDominatingSet(Set<Integer> dominatingSet) {
        this.dominatingSet = new HashSet<>(dominatingSet);
        repaint();
    }

    public void clearDominatingSet() {
        setDominatingSet(new HashSet<>());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (graph == null || graph.getSize() == 0) {
            drawCentered(g2, "Agregá vértices o cargá un grafo desde un archivo", HINT, 15f);
            g2.dispose();
            return;
        }

        int n = graph.getSize();
        Point[] positions = computePositions(n);
        int radius = vertexRadius(n);

        drawEdges(g2, positions);
        drawVertices(g2, positions, radius);
        if (!dominatingSet.isEmpty()) drawLegend(g2);

        g2.dispose();
    }

    private Point[] computePositions(int n) {
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        Point[] positions = new Point[n];

        if (n == 1) {
            positions[0] = new Point(centerX, centerY);
            return positions;
        }

        int layoutRadius = Math.max(40, Math.min(width, height) / 2 - 60);
        for (int i = 0; i < n; i++) {
            double angle = -Math.PI / 2 + 2 * Math.PI * i / n; // el 0 arriba, en sentido horario
            positions[i] = new Point(
                    centerX + (int) Math.round(layoutRadius * Math.cos(angle)),
                    centerY + (int) Math.round(layoutRadius * Math.sin(angle)));
        }
        return positions;
    }

    private int vertexRadius(int n) {
        if (n <= 20) return 20;
        if (n <= 40) return 15;
        return 10;
    }

    private void drawEdges(Graphics2D g2, Point[] positions) {
        g2.setColor(EDGE);
        g2.setStroke(new BasicStroke(2f));
        for (int i = 0; i < positions.length; i++) {
            for (int j = i + 1; j < positions.length; j++) {
                if (graph.edgeExists(i, j)) {
                    g2.drawLine(positions[i].x, positions[i].y, positions[j].x, positions[j].y);
                }
            }
        }
    }

    private void drawVertices(Graphics2D g2, Point[] positions, int radius) {
        Font font = getFont().deriveFont(Font.BOLD, radius >= 15 ? 14f : 10f);
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics();

        for (int i = 0; i < positions.length; i++) {
            Point p = positions[i];
            Ellipse2D circle = new Ellipse2D.Double(p.x - radius, p.y - radius, 2 * radius, 2 * radius);
            boolean dominating = dominatingSet.contains(i);

            g2.setColor(dominating ? DOMINATING_FILL : VERTEX_FILL);
            g2.fill(circle);
            g2.setColor(dominating ? DOMINATING_FILL.darker() : VERTEX_BORDER);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(circle);

            String label = String.valueOf(i);
            g2.setColor(dominating ? Color.WHITE : TEXT);
            g2.drawString(label,
                    p.x - metrics.stringWidth(label) / 2,
                    p.y + (metrics.getAscent() - metrics.getDescent()) / 2);
        }
    }

    private void drawLegend(Graphics2D g2) {
        int x = 16;
        int y = getHeight() - 24;
        g2.setColor(DOMINATING_FILL);
        g2.fillOval(x, y - 10, 14, 14);
        g2.setColor(TEXT);
        g2.setFont(getFont().deriveFont(Font.PLAIN, 13f));
        g2.drawString("Vértice del conjunto dominante", x + 22, y + 2);
    }

    private void drawCentered(Graphics2D g2, String text, Color color, float size) {
        g2.setColor(color);
        g2.setFont(getFont().deriveFont(Font.PLAIN, size));
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(text, (getWidth() - metrics.stringWidth(text)) / 2, getHeight() / 2);
    }
}
