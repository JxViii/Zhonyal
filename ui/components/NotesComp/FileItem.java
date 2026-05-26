package ui.components.NotesComp;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import helpers.FileUtils;
import utils.Theme;

public class FileItem extends JPanel{

  private JLabel title;
  private JLabel size;
  private JLabel icon;
  private File file;
  private boolean hovered = false;
  private boolean delete = false;

  public FileItem( File file, Boolean delete, Consumer<File> onClick, Runnable onDelete ){

    this.file = file;
    this.delete = delete;

    Font font = new Font("Inter 24pt ExtraBold", Font.PLAIN, 18);

    title = new JLabel(clip(this.file.getName(), font, 220));
    title.setFont(font);
    title.setForeground(Theme.BLACK);
    title.setPreferredSize(new Dimension(230, 30));

    size = new JLabel(
      FileUtils.formatSize( this.file.length() )
    );
    size.setFont(font);
    size.setForeground(Theme.GREY);
    
    ImageIcon iconPng = new ImageIcon(
      delete ? "images/Delete.png" : "images/Search.png"
    );
    Image iconRx = iconPng.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
    iconPng = new ImageIcon(iconRx);

    icon = new JLabel();
    icon.setIcon(iconPng);

    setLayout(new GridBagLayout());
    setOpaque(false);
    setPreferredSize(new Dimension(315,103));
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    addMouseListener(new MouseAdapter() {
        @Override public void mouseClicked(MouseEvent e) {
          if(delete) { if (onDelete != null) onDelete.run(); }
          else { if (onClick != null) onClick.accept(file); }
         }
        @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
        @Override public void mouseExited(MouseEvent e)  { hovered = contains(e.getPoint()); repaint(); }
    });

    Border padding = BorderFactory.createEmptyBorder(0,0,0,0);

    setBorder(padding);

    GridBagConstraints gbc = new GridBagConstraints();

    int w = 5;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(-30,w,0,0);
    gbc.anchor = GridBagConstraints.WEST;

    add(title, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(-20,w,0,0);
    gbc.anchor = GridBagConstraints.WEST;

    add(size, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(0,2,0,0);
    gbc.anchor = GridBagConstraints.CENTER;

    add(icon, gbc);

  }

  private String clip(String text, Font font, int maxWidth) {
    FontRenderContext frc = new FontRenderContext(null, true, true);
    if (font.getStringBounds(text, frc).getWidth() <= maxWidth) return text;
    while (text.length() > 0 && font.getStringBounds(text + "...", frc).getWidth() > maxWidth)
      text = text.substring(0, text.length() - 1);
    return text + "...";
  }

  @Override
  public void paintComponent(Graphics g){

    super.paintComponent(g);
    
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int w = getWidth(), h = getHeight();
    int r = 50;

    g2.setColor(
      hovered ? 
      (delete ? Theme.LL_RED : Theme.LILA) :
      Theme.LL_GREY
    );
    g2.fillRoundRect(1,1,w - 3, h - 3, r, r);

    g2.setColor(Theme.GREY);
    g2.setStroke(new BasicStroke(3f));
    g2.drawRoundRect(1, 1, w-3, h-3, r, r);

  }

}
