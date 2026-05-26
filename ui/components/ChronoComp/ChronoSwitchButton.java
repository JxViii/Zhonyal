package ui.components.ChronoComp;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import utils.Theme;

public class ChronoSwitchButton extends JPanel {

    private final BufferedImage icon;
    private ActionListener listener;

    public ChronoSwitchButton() {
        Dimension size = new Dimension(85, 85);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        BufferedImage loaded = null;
        try {
            BufferedImage raw = ImageIO.read(new File("images/logo-white.png"));
            loaded = new BufferedImage(41, 57, BufferedImage.TYPE_INT_ARGB);
            Graphics2D ig = loaded.createGraphics();
            ig.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            ig.drawImage(raw, 0, 0, 41, 57, null);
            ig.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
        icon = loaded;

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
        int d = 79; // fixed 85px oval minus stroke padding
        int x = (w - d) / 2, y = (h - d) / 2;

        // border
        g2.setColor(Theme.WHITE);
        g2.setStroke(new BasicStroke(4.5f));
        g2.drawOval(x, y, d, d);

        // icon centered within the oval
        if (icon != null) {
            int iw = icon.getWidth(), ih = icon.getHeight();
            g2.drawImage(icon, x + (d - iw) / 2, y + (d - ih) / 2, null);
        }

        g2.dispose();
    }
}
