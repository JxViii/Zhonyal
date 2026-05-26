package ui.components.SessionDetailsComp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

import helpers.Functions;
import helpers.LangManager;
import helpers.Split;
import helpers.Type;
import utils.Theme;

public class SplitItem extends JPanel{
  
  private JLabel title;
  private JLabel start;
  private JLabel end;
  private JLabel time;
  private JLabel type;

  private Color color;

  public SplitItem( Split split ){

    title = new JLabel(split.getTitle());
    start = new JLabel(
      Functions.formatDateTime( split.getStartDate() )
    );
    end = new JLabel(
      Functions.formatDateTime( split.getEndDate() )
    );
    time = new JLabel( Functions.formatDuration(split.getDuration())); 
    type = new JLabel( (split.getType() == Type.STUDY) ? LangManager.get("split.study") : LangManager.get("split.pause") );
    color = ( split.getType() == Type.STUDY ) ?
            Theme.LL_GREEN : Theme.L_RED;

    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    setOpaque(false);

    Font titleFont = new Font("Inter 24pt ExtraBold", Font.PLAIN, 20);
    Font dateFont = new Font("Inter 18pt", Font.BOLD, 14);
    Font timeFont = new Font("Inter 24pt ExtraBold", Font.BOLD, 35);
    Font typeFont = new Font("Inter 18pt", Font.BOLD, 18);

    int wcol1 = 0;
    int hdate = 10;
    int wcol2 = 80;
    int hcol2 = -20;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(0,wcol1,0,0);
    gbc.anchor = GridBagConstraints.WEST;

    title.setForeground(Theme.WHITE);
    title.setFont(titleFont);

    add(title, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(hdate,wcol1,0,0);

    start.setForeground(Theme.L_GREY);
    start.setFont(dateFont);
    end.setForeground(Theme.L_GREY);
    end.setFont(dateFont);

    add(start, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(0,wcol1,0,0);

    add(end, gbc);
    
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.insets = new Insets(hcol2,wcol2,0,0);

    time.setForeground( color );
    time.setFont(timeFont);
    time.setPreferredSize(new Dimension(160, 48));

    add(time, gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.insets = new Insets(0,wcol2,0,0);

    type.setForeground( Theme.LL_GREY );
    type.setFont(typeFont);

    add(type, gbc);
    
    Dimension d = new Dimension(435, 127);
    setPreferredSize(d);
    setMinimumSize(d);
    setMaximumSize(d);

    // Back button:


  }

  @Override
  public void paintComponent(Graphics g){
    
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g.create();

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int w = getWidth(), h = getHeight();
    int r = 60;

    g2.setColor(Theme.BLACK);
    g2.fillRoundRect(1, 1, w - 2, h - 2, r, r);

    g2.setColor(Theme.WHITE);
    g2.setStroke(new BasicStroke(2.5f));
    g2.drawRoundRect(1, 1, w - 2, h - 2, r, r);

    g2.dispose();

  }


}
