package ui.components.HomeComp;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class TextAddButton extends JButton {

  private final String text;
  private final Font font = new Font("BBH Hegarty", Font.PLAIN, 20);
  private final Color color;

  public TextAddButton(
    String text,
    Color color
  ){

    super(text);

    this.text = text.toUpperCase();
    this.color = color;

    setOpaque(false);
    setContentAreaFilled(false);
    setBorderPainted(false);
    setFocusPainted(false);
    setEnabled(false);
    setFont(font);
    setMargin(new Insets(2, 20, 2, 20));
    setPreferredSize(new Dimension(240,55));

  }

  @Override
  protected void paintComponent(Graphics g){
    
    Graphics2D g2 = (Graphics2D) g.create();

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    
    g2.setColor(this.color);
    g2.setStroke(new BasicStroke(3f));
    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 50, 50);

    g2.setFont(this.font);
    g2.setColor(this.color);
    FontMetrics fm = g2.getFontMetrics();

    int textWidth = fm.stringWidth(this.text);
    int circleSize = 24;
    int gap = 12;
    int totalWidth = textWidth + gap + circleSize;

    int textX = (getWidth() - totalWidth) / 2;
    int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
    g2.drawString(text, textX, textY);

    // + circle
    int circleX = textX + textWidth + gap;
    int circleY = (getHeight() - circleSize) / 2;
    g2.setStroke(new BasicStroke(3f));
    g2.drawOval(circleX, circleY, circleSize, circleSize);

    // + lines
    int plusPad = 6;
    int cx = circleX + circleSize / 2;
    int cy = circleY + circleSize / 2;
    g2.drawLine(cx - plusPad, cy, cx + plusPad, cy);
    g2.drawLine(cx, cy - plusPad, cx, cy + plusPad);

    g2.dispose();

  }
  
  public void setSize(Dimension dimension){ this.setPreferredSize(dimension); repaint();}
  
}