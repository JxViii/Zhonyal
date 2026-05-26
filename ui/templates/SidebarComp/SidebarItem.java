package ui.templates.SidebarComp;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

import utils.Theme;

public class SidebarItem extends JButton{

  private final Font font;
  private boolean active = false;

  public void setActive(boolean active) {
      this.active = active;
      repaint();
  }

  public boolean isActive() {
      return active;
  }

  public SidebarItem(
    String text
  ){

    font = new Font("BBH Hegarty", Font.PLAIN, 19);

    setOpaque(false);
    setContentAreaFilled(false);
    setBorderPainted(false);
    setFocusPainted(false);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    setPreferredSize(getPreferredSize());
    setText(text);
    setFont(font);

    Dimension size = new Dimension(170, 55);
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);

  }
  
  @Override
  protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      // Fill only when active
      if (active) {
          g2.setColor(Theme.HIGHLIGHT_BUTTON);
          g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 22, 22);
      }

      // Border always
      g2.setColor(Theme.WHITE);
      g2.setStroke(new BasicStroke(2f));
      g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 25, 25);

      // Text always
      g2.setFont(font);
      g2.setColor(Theme.WHITE);
      FontMetrics fm = g2.getFontMetrics();
      int textX = 17;
      int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
      g2.drawString(getText(), textX, textY);

      g2.dispose();
  }

}
