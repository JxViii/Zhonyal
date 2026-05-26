package ui.components.SessionsHomeComp;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

import helpers.LangManager;
import utils.Theme;

public class SessionsViewButton extends JPanel {

    private ActionListener actionListener;
    private final Font labelFont = new Font("BBH Bartle", Font.PLAIN, 26);
    public SessionsViewButton() {
        Dimension size = new Dimension(520, 75);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (actionListener != null)
                    actionListener.actionPerformed(null);
            }
        });
    }

    public void addActionListener(ActionListener l) { this.actionListener = l; }

    public void refreshText() { repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        float x = 2, y = 2, r = Math.min(150f, h / 2f);
        float w2 = w - 4, h2 = h - 4;

        Path2D.Float path = buildPath(x, y, w2, h2, r);

        // border
        g2.setColor(Theme.WHITE);
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(path);

        // label
        g2.setFont(labelFont);
        g2.setColor(Theme.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        String text = LangManager.get("sessions.view.btn");
        int tx = (w - fm.stringWidth(text)) / 2;
        int ty = (h + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(text, tx, ty);

        g2.dispose();
    }

    private Path2D.Float buildPath(float x, float y, float w2, float h2, float r) {
        Path2D.Float path = new Path2D.Float();
        path.moveTo(x + r, y);
        path.lineTo(x + w2 - r, y);
        path.quadTo(x + w2, y, x + w2, y + r);        // top-right rounded
        path.lineTo(x + w2, y + h2 - r);
        path.quadTo(x + w2, y + h2, x + w2 - r, y + h2); // bottom-right rounded
        path.lineTo(x, y + h2);                        // bottom edge → sharp bottom-left
        path.lineTo(x, y + r);
        path.quadTo(x, y, x + r, y);                   // top-left rounded
        path.closePath();
        return path;
    }
}
