package ui.templates.HeaderComp;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;

import utils.Theme;

public class HeaderMenu extends JButton {
  
  public HeaderMenu(){

    setOpaque(false);
    setContentAreaFilled(false);
    setBorderPainted(false);
    setFocusPainted(false);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

  }

  @Override
  protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setColor(Theme.WHITE);
      g2.setStroke(new BasicStroke(2f));
      int w = getWidth();
      int h = getHeight();
      int lineW = 24;
      int x = (w - lineW) / 2;
      g2.drawLine(x, h/2 - 7, x + lineW, h/2 - 7);
      g2.drawLine(x, h/2,     x + lineW, h/2);
      g2.drawLine(x, h/2 + 7, x + lineW, h/2 + 7);
      g2.dispose();
  }

}
