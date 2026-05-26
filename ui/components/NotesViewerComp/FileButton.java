package ui.components.NotesViewerComp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;

import javax.swing.JButton;

import utils.Theme;

public class FileButton extends JButton{
  
  private File file;
  private Boolean selected = false;

  public FileButton( String title, File f ){

    this.file = f;

    Font filesFont = new Font("Inter 18pt", Font.BOLD, 18);

    setText(title);
    setOpaque(false);
    setContentAreaFilled(false);
    setBorderPainted(false);
    setFocusPainted(false);
    setMargin(new Insets(2,10,2,10));
    setFont(filesFont);
    Color fg = isActive() ? Theme.WHITE : Theme.L_GREY;
    setForeground(fg);
    setAlignmentX(Component.LEFT_ALIGNMENT);
    setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


  }

  public File getFile(){ return this.file; }
  public Boolean isActive(){ return this.selected; }
  public void setActive( Boolean b ){ this.selected = b; }
  public void recolor(){
    Color fg = isActive() ? Theme.WHITE : Theme.L_GREY;
    setForeground(fg);
    repaint();
  }

}

