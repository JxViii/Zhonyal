package ui.components.SessionsManagerComp;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import helpers.Functions;
import helpers.LangManager;
import helpers.Session;
import utils.Theme;

public class SessionItem extends JPanel{
  
  private Session session;
  private boolean delete = false;
  private boolean hovered = false;

  private JPanel header;
  private JPanel main;

  private JLabel title;
  private JPanel datePanel;
  private JLabel startDate;
  private JLabel endDate;

  private JPanel totalPanel;
  private JPanel studyPanel;
  private JPanel pausePanel;

  private JLabel totalTime;
  private JLabel studyTime;
  private JLabel pauseTime;

  private JLabel tLabel = new JLabel(LangManager.get("session.item.total"));
  private JLabel sLabel = new JLabel(LangManager.get("session.item.study"));
  private JLabel pLabel = new JLabel(LangManager.get("session.item.pause"));

  public SessionItem( Session s, boolean delete, Consumer<Session> onClick, Runnable onDelete ){

    this.session = s;
    this.delete  = delete;

    Dimension d = new Dimension(906,143);

    Border padding = BorderFactory.createEmptyBorder(21,40,21,40);

    setBorder(padding);
    setPreferredSize(d);
    setMaximumSize(d);
    setMinimumSize(d);
    setLayout(new BorderLayout());
    setOpaque(false);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    addMouseListener(new MouseAdapter() {
        @Override public void mouseClicked(MouseEvent e) {
            if (delete) { if (onDelete != null) onDelete.run(); }
            else        { if (onClick  != null) onClick.accept(session); }
        }
        @Override public void mouseEntered(MouseEvent e) { hover(); }
        @Override public void mouseExited(MouseEvent e)  { dfl();  }
    });

    header = new JPanel(new BorderLayout());
    header.setOpaque(false);
    header.setPreferredSize(new Dimension(826, 39));

    Font titleFont = new Font("Inter 24pt ExtraBold", Font.PLAIN, 18);
    Font dateFont = new Font("Inter 18pt", Font.BOLD, 14);

    title = new JLabel();
    title.setText( session.getTitle() );
    title.setFont(titleFont);

    datePanel = new JPanel(new FlowLayout());
    datePanel.setOpaque(false);

    startDate = new JLabel();
    endDate = new JLabel();

    String sd = Functions.formatDateTime(session.getStartDate());
    String ed = Functions.formatDateTime(session.getEndDate());

    startDate.setText( sd );
    startDate.setFont(dateFont);

    JLabel arrow = new JLabel("→");

    endDate.setText( ed );
    endDate.setFont(dateFont);

    datePanel.add(startDate);
    datePanel.add(arrow);
    datePanel.add(endDate);

    header.add(title, BorderLayout.WEST);
    header.add(datePanel, BorderLayout.EAST);

    add(header, BorderLayout.NORTH);

    // TIMES

    main = new JPanel();
    main.setLayout(new GridLayout(1, 3, 20, 0));

    String total = Functions.formatDuration( session.getTotalTime() );
    String study = Functions.formatDuration( session.getStudyTime() );
    String pause = Functions.formatDuration( session.getPauseTime() );

    totalTime = new JLabel(total);
    studyTime = new JLabel(study);
    pauseTime = new JLabel(pause);

    Font timeFont  = new Font("Inter 24pt ExtraBold", Font.BOLD, 24);
    Font labelFont = new Font("Inter 18pt", Font.BOLD, 12);

    totalTime.setFont(timeFont);  
    studyTime.setFont(timeFont);  
    pauseTime.setFont(timeFont);  

    tLabel.setFont(labelFont); 
    sLabel.setFont(labelFont); 
    pLabel.setFont(labelFont); 

    totalPanel = new JPanel(new GridBagLayout());
    studyPanel = new JPanel(new GridBagLayout());
    pausePanel = new JPanel(new GridBagLayout());

    Border topShort = BorderFactory.createEmptyBorder(10,0,10,0);

    totalPanel.setOpaque(false);
    studyPanel.setOpaque(false);
    pausePanel.setOpaque(false);

    dfl();

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    totalPanel.add(totalTime, gbc);
    studyPanel.add(studyTime, gbc);
    pausePanel.add(pauseTime, gbc);

    gbc.gridy = 1;
    totalPanel.add(tLabel, gbc);
    studyPanel.add(sLabel, gbc);
    pausePanel.add(pLabel, gbc);

    main.setOpaque(false);
    main.add(totalPanel);
    main.add(studyPanel);
    main.add(pausePanel);

    main.setBorder(topShort);

    add(main, BorderLayout.CENTER);

  }

  public void hover() {
    hovered = true;  repaint();

    title.setForeground(Theme.GREY);
    startDate.setForeground(Theme.GREY);
    endDate.setForeground(Theme.GREY);
    totalTime.setForeground(Theme.D_LILA);
    studyTime.setForeground(Theme.L_GREEN);
    pauseTime.setForeground(Theme.D_RED);
    tLabel.setForeground(Theme.GREY);
    pLabel.setForeground(Theme.GREY);
    sLabel.setForeground(Theme.GREY);

    Border leftBorder = BorderFactory.createCompoundBorder(
      BorderFactory.createMatteBorder(0,2,0,0, Theme.L_GREY),
      BorderFactory.createEmptyBorder(20,20,20,0)
    );

    studyPanel.setBorder(leftBorder);
    pausePanel.setBorder(leftBorder);

  }
  public void dfl() {
    hovered = false; repaint();

    title.setForeground(Theme.WHITE);
    startDate.setForeground(Theme.LL_GREY);
    endDate.setForeground(Theme.LL_GREY);
    totalTime.setForeground(Theme.LILA);
    studyTime.setForeground(Theme.LL_GREEN);
    pauseTime.setForeground(Theme.L_RED);
    tLabel.setForeground(Theme.LL_GREY);
    pLabel.setForeground(Theme.LL_GREY);
    sLabel.setForeground(Theme.LL_GREY);

    Border leftBorder = BorderFactory.createCompoundBorder(
      BorderFactory.createMatteBorder(0,2,0,0, Theme.LL_GREY),
      BorderFactory.createEmptyBorder(20,20,20,0)
    );

    studyPanel.setBorder(leftBorder);
    pausePanel.setBorder(leftBorder);
    
  }

  @Override
  public void paintComponent(Graphics g){

    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int w, h;
    w = getWidth();
    h = getHeight();

    g2.setColor(hovered ?
      delete ? Theme.PINK : Theme.L_BEIGE 
      :
      Theme.BLACK);
    g2.fillRoundRect(1, 1, w - 2, h - 2, 30, 30);

    g2.setColor(hovered && delete? Theme.L_RED : Theme.LL_GREY);
    g2.setStroke(new BasicStroke(2f));
    g2.drawRoundRect(1, 1, w - 2, h - 2, 30, 30);

    g2.dispose();

  }

}
