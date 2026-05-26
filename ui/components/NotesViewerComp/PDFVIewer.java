package ui.components.NotesViewerComp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import utils.Theme;

public class PDFVIewer extends JPanel {

    private static final float BASE_DPI  = 150f;
    private static final float ZOOM_STEP = 0.1f;
    private static final float ZOOM_MIN  = 0.3f;
    private static final float ZOOM_MAX  = 2.0f;

    private final JPanel content;
    private final JPanel pagesPanel;
    private final JScrollPane scroll;
    private final JLabel zoomLabel;

    private List<BufferedImage> rawPages = new ArrayList<>();
    private float scale = 1.0f;

    public PDFVIewer() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Content area — swaps between status panel and scroll
        content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        add(content, BorderLayout.CENTER);

        // Pages panel inside scroll
        pagesPanel = new JPanel();
        pagesPanel.setLayout(new BoxLayout(pagesPanel, BoxLayout.Y_AXIS));
        pagesPanel.setBackground(Theme.BLACK);

        scroll = new JScrollPane(pagesPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BLACK);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Zoom toolbar
        Font btnFont = new Font("Inter 18pt", Font.BOLD, 15);

        JButton zoomOut = makeZoomBtn("-");
        JButton zoomIn  = makeZoomBtn("+");

        zoomLabel = new JLabel(zoomText());
        zoomLabel.setFont(btnFont);
        zoomLabel.setForeground(Theme.L_GREY);

        zoomOut.addActionListener(e -> zoom(-ZOOM_STEP));
        zoomIn.addActionListener(e  -> zoom(+ZOOM_STEP));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        toolbar.setOpaque(true);
        toolbar.setBackground(Theme.BLACK);
        toolbar.add(zoomOut);
        toolbar.add(zoomLabel);
        toolbar.add(zoomIn);

        add(toolbar, BorderLayout.SOUTH);

        showStatus("Select a file to preview");
    }

    public void loadFile(File f) {
        rawPages.clear();
        pagesPanel.removeAll();
        showStatus("Rendering…");

        new SwingWorker<List<BufferedImage>, Void>() {
            @Override
            protected List<BufferedImage> doInBackground() throws Exception {
                List<BufferedImage> pages = new ArrayList<>();
                try (PDDocument doc = Loader.loadPDF(f)) {
                    PDFRenderer renderer = new PDFRenderer(doc);
                    for (int i = 0; i < doc.getNumberOfPages(); i++)
                        pages.add(renderer.renderImageWithDPI(i, BASE_DPI));
                }
                return pages;
            }

            @Override
            protected void done() {
                try {
                    rawPages = get();
                    showScroll();
                    SwingUtilities.invokeLater(() -> {
                        int vw = scroll.getViewport().getWidth();
                        if (vw > 0 && !rawPages.isEmpty()) {
                            scale = Math.round(((float) vw / rawPages.get(0).getWidth()) * 100f) / 100f;
                            scale -= .02;
                            zoomLabel.setText(zoomText());
                        }
                        refreshPages();
                    });
                } catch (InterruptedException | ExecutionException ex) {
                    showStatus("Failed to render: " + ex.getCause().getMessage());
                }
            }
        }.execute();
    }

    private void zoom(float delta) {
        float next = Math.round((scale + delta) * 100f) / 100f;
        if (next < ZOOM_MIN || next > ZOOM_MAX || rawPages.isEmpty()) return;
        scale = next;
        zoomLabel.setText(zoomText());
        refreshPages();
    }

    private void refreshPages() {
        pagesPanel.removeAll();
        for (BufferedImage raw : rawPages) {
            int w = Math.round(raw.getWidth()  * scale);
            int h = Math.round(raw.getHeight() * scale);
            JLabel page = new JLabel(new ImageIcon(scaleImage(raw, w, h)));
            page.setAlignmentX(CENTER_ALIGNMENT);
            pagesPanel.add(Box.createVerticalStrut(10));
            pagesPanel.add(page);
        }
        pagesPanel.add(Box.createVerticalStrut(10));
        pagesPanel.revalidate();
        pagesPanel.repaint();
    }

    private BufferedImage scaleImage(BufferedImage src, int w, int h) {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = out.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,     RenderingHints.VALUE_RENDER_QUALITY);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();
        return out;
    }

    private JButton makeZoomBtn(String label) {
        JButton b = new JButton(label);
        b.setFont(new Font("Inter 18pt", Font.BOLD, 16));
        b.setForeground(Theme.WHITE);
        b.setBackground(Theme.LOGOUT_BUTTON);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(45, 35));
        return b;
    }

    private String zoomText() {
        return Math.round(scale * 100) + "%";
    }

    private void showStatus(String msg) {
        content.removeAll();
        JLabel label = new JLabel(msg);
        label.setForeground(Theme.L_GREY);
        label.setFont(new Font("Inter 18pt", Font.PLAIN, 16));
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        wrap.add(label);
        content.add(wrap, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    private void showScroll() {
        content.removeAll();
        content.add(scroll, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

}
