package ui.components.ChronoComp;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Theme;

public class ChronoAddButton extends JPanel {

    // private static final Color FILL = new Color(0x252525);
    private ActionListener listener;
    private JLabel collapseLabel;
    private ImageIcon collapse;

    public ChronoAddButton() {

        collapse = new ImageIcon("images/collapse.png");
        Image iconRx = collapse.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        collapse = new ImageIcon(iconRx);
        collapseLabel = new JLabel();
        collapseLabel.setIcon(collapse);

        add(collapseLabel);

        Dimension size = new Dimension(59, 59);
        setLayout(new GridBagLayout());
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (listener != null) listener.actionPerformed(null);
            }
        });
    }

    public void addActionListener(ActionListener l) { this.listener = l; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        int pad = 3;
        int d = Math.min(w, h) - pad * 2;
        int x = (w - d) / 2, y = (h - d) / 2;

        // fill
        // g2.setColor(FILL);
        // g2.fillOval(x, y, d, d);

        // border
        g2.setColor(Theme.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawOval(x, y, d, d);

        // + symbol
        // int cx = w / 2, cy = h / 2, arm = d / 5;
        // g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // g2.setColor(Theme.WHITE);
        // g2.drawLine(cx - arm, cy, cx + arm, cy);
        // g2.drawLine(cx, cy - arm, cx, cy + arm);

        g2.dispose();
    }
}
