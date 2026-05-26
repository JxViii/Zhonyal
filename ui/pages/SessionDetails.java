package ui.pages;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import helpers.Functions;
import helpers.LangManager;
import helpers.Session;
import helpers.Split;
import ui.components.SessionDetailsComp.SplitItem;
import utils.Theme;

public class SessionDetails extends JPanel{

  private JPanel hero;
  private JPanel aux;
  private JPanel main;
  private JPanel col1;
  private JPanel col2;

  private List<Split> splits;

  private List<SplitItem> splitItems;

  private JLabel title;
  private JLabel desc;

  private JLabel sTitle;
  private JLabel startDate;
  private JLabel startTime;
  private JLabel endDate;
  private JLabel endTime;
  private JLabel totalTime;
  private JLabel studyTime;
  private JLabel pauseTime;
  private JLabel focusRate;

  private JButton back;
  private Runnable onBack;

  public void setOnBack(Runnable r) { this.onBack = r; }

  public SessionDetails( Session session ){

    if(session == null) return;

    this.splits = session.getSplits();

    setLayout(new BorderLayout());
    setOpaque(false);

    //  hero

    hero = new JPanel(new GridBagLayout());
    hero.setOpaque(false);

    Border heroPadding = BorderFactory.createEmptyBorder(60,10,20,10);

    hero.setBorder(heroPadding);
    hero.setPreferredSize(new Dimension(1240, 192));

    Font titleFont = new Font("BBH Bartle", Font.PLAIN, 48);
    Font descFont = new Font("Inter 24pt ExtraBold", Font.PLAIN, 24);

    title = new JLabel(LangManager.get("session.details.title"));
    title.setForeground(Theme.WHITE);
    title.setFont(titleFont);

    desc = new JLabel(LangManager.get("session.details.desc"));
    desc.setForeground(Theme.L_GREY);
    desc.setFont(descFont);

    GridBagConstraints gbc = new GridBagConstraints();

    back = new JButton(LangManager.get("session.details.back"));
    back.setForeground(Theme.LL_GREY);
    back.setFont(new Font("Inter 18pt", Font.BOLD, 18));
    back.setContentAreaFilled(false);
    back.setBorderPainted(false);
    back.setFocusPainted(false);
    back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    back.addActionListener(e -> { if (onBack != null) onBack.run(); });

    // col 0: back button, NORTHWEST, weighted left
    gbc.gridx = 0; gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.insets = new Insets(10, -70, 0, 0);
    hero.add(back, gbc);

    // col 1: title + desc, centered, tight
    gbc.gridx = 1; gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(0, 70, 0, 0);
    hero.add(title, gbc);

    gbc.gridy = 1;
    gbc.insets = new Insets(20, 0, 0, 0);
    hero.add(desc, gbc);
    
    add(hero, BorderLayout.NORTH);

    //main

    main = new JPanel(new GridLayout(1,2,20,0));
    main.setOpaque(false);
    // main.setBackground(Theme.L_GREY);
    main.setPreferredSize(new Dimension(1200, 757));

    Border mainPadding = BorderFactory.createEmptyBorder(20,60,20,60);

    main.setBorder(mainPadding);

    col1 = new JPanel();
    col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));
    col1.setOpaque(false);
    col1.setBorder(BorderFactory.createEmptyBorder(5, 40, 40, 40));

    Font sFont = new Font("Inter 24pt ExtraBold", Font.BOLD, 25);

    sTitle = new JLabel( "@" + session.getTitle() );
    sTitle.setForeground(Theme.LL_GREY);
    sTitle.setFont(sFont);
    sTitle.setAlignmentX(JPanel.LEFT_ALIGNMENT);

    JPanel grid = new JPanel();
    grid.setLayout(new BoxLayout(grid, BoxLayout.Y_AXIS));
    grid.setOpaque(false);
    grid.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

    splitItems = splits.stream()
                        .map( s -> new SplitItem(s))
                        .toList();

    for( SplitItem s : splitItems ){
      grid.add(s);
      grid.add(Box.createVerticalStrut(22));
    }

    int hGrid = 160 * splitItems.size();
    grid.setPreferredSize(new Dimension(435, hGrid));

    JScrollPane scroll = new JScrollPane(grid);
    scroll.setOpaque(false);
    scroll.getViewport().setOpaque(false);
    scroll.setBorder(null);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    scroll.setPreferredSize(new Dimension(475, 640));
    // scroll.setMinimumSize(new Dimension(475, 640));
    scroll.setMaximumSize(new Dimension(475, Integer.MAX_VALUE));
    scroll.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    scroll.addMouseWheelListener(e -> {
      JScrollBar bar = scroll.getVerticalScrollBar();
      bar.setValue(bar.getValue() + e.getUnitsToScroll() * 10);
    });

    col1.add(sTitle);
    col1.add(Box.createVerticalStrut(40));
    col1.add(scroll);

    main.add(col1);

    //col2

    col2 = new JPanel(new GridBagLayout());
    col2.setOpaque(false);

    JPanel statsGrid = new JPanel(new GridBagLayout());
    statsGrid.setOpaque(false);

    Font dateFont  = new Font("Inter 24pt ExtraBold", Font.BOLD, 26);
    Font timeFont  = new Font("Inter 24pt ExtraBold", Font.BOLD, 23);
    Font lblFont   = new Font("Inter 18pt", Font.BOLD, 20);
    Font totalFont = new Font("Inter 24pt ExtraBold", Font.BOLD, 50);
    Font splitFont = new Font("Inter 24pt ExtraBold", Font.BOLD, 41);
    Font focusFont = new Font("Inter 24pt ExtraBold", Font.BOLD, 59);

    startDate = new JLabel( Functions.formatDate( session.getStartDate() ));
    startTime = new JLabel( Functions.formatTime( session.getStartDate() ));
    endDate   = new JLabel( Functions.formatDate( session.getEndDate() ));
    endTime   = new JLabel( Functions.formatTime( session.getEndDate() ));
    totalTime = new JLabel( Functions.formatDuration( session.getTotalTime() ));
    studyTime = new JLabel( Functions.formatDuration( session.getStudyTime() ));
    pauseTime = new JLabel( Functions.formatDuration( session.getPauseTime() ));
    focusRate = new JLabel( String.format("%.1f%%", session.getFocusRate()) );

    startDate.setFont(dateFont);  startDate.setForeground(Theme.WHITE);
    startTime.setFont(timeFont);  startTime.setForeground(Theme.LL_GREY);
    endDate.setFont(dateFont);    endDate.setForeground(Theme.WHITE);
    endTime.setFont(timeFont);    endTime.setForeground(Theme.LL_GREY);
    totalTime.setFont(totalFont); totalTime.setForeground(Theme.LILA);
    studyTime.setFont(splitFont); studyTime.setForeground(Theme.LL_GREEN);
    pauseTime.setFont(splitFont); pauseTime.setForeground(Theme.L_RED);
    focusRate.setFont(focusFont); focusRate.setForeground(Theme.CYAN);

    JLabel lStart  = new JLabel(LangManager.get("session.details.start"));
    JLabel lEnd    = new JLabel(LangManager.get("session.details.end"));
    JLabel lFull   = new JLabel(LangManager.get("session.details.full"));
    JLabel lStudy  = new JLabel(LangManager.get("session.details.study"));
    JLabel lPause  = new JLabel(LangManager.get("session.details.pause"));
    JLabel lFocus  = new JLabel(LangManager.get("session.details.focus"));
    JLabel divider = new JLabel("|");

    lStart.setFont(lblFont);  lStart.setForeground(Theme.L_GREY);
    lEnd.setFont(lblFont);    lEnd.setForeground(Theme.L_GREY);
    lFull.setFont(lblFont);   lFull.setForeground(Theme.L_GREY);
    lStudy.setFont(lblFont);  lStudy.setForeground(Theme.L_GREY);
    lPause.setFont(lblFont);  lPause.setForeground(Theme.L_GREY);
    lFocus.setFont(lblFont);  lFocus.setForeground(Theme.L_GREY);
    divider.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 40));
    divider.setForeground(Theme.LOGOUT_BUTTON);

    GridBagConstraints c = new GridBagConstraints();

    // Row 0: startDate | divider | endDate
    c.gridx = 0; c.gridy = 0; c.gridwidth = 1;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(0, 30, 0, 0);
    statsGrid.add(startDate, c);

    c.gridx = 1; c.anchor = GridBagConstraints.CENTER;
    c.insets = new Insets(0, 20, 0, 20);
    statsGrid.add(divider, c);

    c.gridx = 2; c.anchor = GridBagConstraints.EAST;
    c.insets = new Insets(0, 0, 0, 30);
    statsGrid.add(endDate, c);

    // Row 1: startTime | | endTime
    c.gridx = 0; c.gridy = 1;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(4, 30, 0, 0);
    statsGrid.add(startTime, c);

    c.gridx = 2; c.anchor = GridBagConstraints.EAST;
    c.insets = new Insets(4, 0, 0, 30);
    statsGrid.add(endTime, c);

    // Row 2: START | | END
    c.gridx = 0; c.gridy = 2;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(4, 30, 0, 0);
    statsGrid.add(lStart, c);

    c.gridx = 2; c.anchor = GridBagConstraints.EAST;
    c.insets = new Insets(4, 0, 0, 30);
    statsGrid.add(lEnd, c);

    // Row 3: totalTime centered, spans 3
    c.gridx = 0; c.gridy = 3; c.gridwidth = 3;
    c.anchor = GridBagConstraints.CENTER;
    c.insets = new Insets(50, 0, 0, 0);
    statsGrid.add(totalTime, c);

    // Row 4: FULL
    c.gridy = 4; c.insets = new Insets(4, 0, 0, 0);
    statsGrid.add(lFull, c);

    // Row 5: studyTime | | pauseTime
    c.gridwidth = 1;
    c.gridx = 0; c.gridy = 5;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(40, 30, 0, 0);
    statsGrid.add(studyTime, c);

    c.gridx = 2; c.anchor = GridBagConstraints.EAST;
    c.insets = new Insets(40, 0, 0, 30);
    statsGrid.add(pauseTime, c);

    // Row 6: STUDY | | PAUSE
    c.gridx = 0; c.gridy = 6;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(4, 30, 0, 0);
    statsGrid.add(lStudy, c);

    c.gridx = 2; c.anchor = GridBagConstraints.EAST;
    c.insets = new Insets(4, 0, 0, 30);
    statsGrid.add(lPause, c);

    // Row 7: focusRate centered, spans 3
    c.gridx = 0; c.gridy = 7; c.gridwidth = 3;
    c.anchor = GridBagConstraints.CENTER;
    c.insets = new Insets(50, 0, 0, 0);
    statsGrid.add(focusRate, c);

    // Row 8: FOCUS RATE
    c.gridy = 8; c.insets = new Insets(4, 0, 0, 0);
    statsGrid.add(lFocus, c);

    c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.NORTH;
    c.insets = new Insets(0,0,0,0);

    col2.add(statsGrid, c);

    // FIXED ERROR WITH main size

    main.setPreferredSize(new Dimension(1240,757));
    main.setMaximumSize(new Dimension(1240,757));
    main.setMinimumSize(new Dimension(1240,757));

    main.add(col2);

    aux = new JPanel(new GridBagLayout());
    aux.setOpaque(false);

    aux.add(main);

    add(aux, BorderLayout.CENTER);

  }

}
