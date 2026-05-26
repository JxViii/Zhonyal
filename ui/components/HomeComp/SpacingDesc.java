package ui.components.HomeComp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class SpacingDesc extends JPanel {

    private String desc;
    private final Color color;
    private final float letterSpacing = 2.6f;
    private final Font font = new Font("Inter 18pt", Font.PLAIN, 26);

    public SpacingDesc(String desc, Color color) {
        this.desc = desc;
        this.color = color;
        setOpaque(false);
    }

    public void setText(String d) { this.desc = d; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setFont(font);
        g2.setColor(color);

        FontMetrics fm = g2.getFontMetrics();
        int maxWidth = getWidth();

        float x = 0;
        float y = fm.getAscent();
        String[] words = desc.split(" ");

        for (int w = 0; w < words.length; w++) {
            String word = words[w];
            float wordWidth = measureWord(word, fm);

            if (x + wordWidth > maxWidth && x > 0) {
                x = 0;
                y += fm.getHeight();
            }

            for (char c : word.toCharArray()) {
                g2.drawString(String.valueOf(c), x, y);
                x += fm.charWidth(c) + letterSpacing;
            }

            if (w < words.length - 1) {
                x += fm.charWidth(' ');
            }
        }

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(font);
        int maxWidth = getParent() != null ? getParent().getWidth() : 300;

        int lines = 1;
        float x = 0;
        String[] words = desc.split(" ");
        for (int w = 0; w < words.length; w++) {
            float wordWidth = measureWord(words[w], fm);
            if (x + wordWidth > maxWidth && x > 0) {
                lines++;
                x = 0;
            }
            x += wordWidth + fm.charWidth(' ');
        }

        return new Dimension(maxWidth, lines * fm.getHeight());
    }

    private float measureWord(String word, FontMetrics fm) {
        float width = 0;
        for (int i = 0; i < word.length(); i++) {
            width += fm.charWidth(word.charAt(i));
            if (i < word.length() - 1) width += letterSpacing;
        }
        return width;
    }
}
