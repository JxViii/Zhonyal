package ui.pages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.border.Border;

import helpers.Functions;
import helpers.LangManager;
import helpers.Session;
import helpers.Split;
import ui.components.ChronoComp.ChronoAddButton;
import ui.components.ChronoComp.ChronoStopButton;
import ui.components.ChronoComp.ChronoSwitchButton;
import utils.Theme;

public class Chrono extends JWindow {

  private Session session;

  private JPanel header;
  private JPanel main;
  private JPanel buttons;

  private JLabel title;
  private JPanel datePanel;
  private JLabel startDate;
  // Need the -> too
  private JLabel endDate;

  private JLabel totalTime;
  private JLabel splitTime;
  private JLabel splitType;

  private ChronoSwitchButton swap;
  private ChronoAddButton collapse;
  private ChronoStopButton stop;
  private Timer ticker;

  private Boolean isCollapsed;
  private final JPanel filler = new JPanel();

  public Chrono(Frame owner, Session session) {

    super(owner);

    this.session = session;
    isCollapsed = false;

    setSize(442, 304);
    setLocationRelativeTo(null);

    setAlwaysOnTop(true);
    getContentPane().setBackground(Theme.BLACK);
    setLayout(new BorderLayout());

    //header
    setUpHeader();

    //main
    setUpMain();

    //buttons
    setUpButtons();

    addComponentListener(new ComponentAdapter() {
      @Override public void componentResized(ComponentEvent e) {
        int expectedH = isCollapsed ? 124 : 304;
        if (getHeight() != expectedH) setSize(442, expectedH);
      }
    });

    ticker = new Timer(1000, e -> {
      totalTime.setText(fmt(session.getTotalTime()));
      splitTime.setText("+ " + fmt(session.getCurrent().getDuration()));
      endDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
    });
    ticker.start();

  }

  public void collapse() {
    getContentPane().remove(header);
    getContentPane().remove(main);
    getContentPane().remove(buttons);
    getContentPane().add(buttons, BorderLayout.NORTH);
    setMinimumSize(new Dimension(442, 124));
    setMaximumSize(new Dimension(442, 124));
    setSize(442, 124);
    isCollapsed = true;
    
    revalidate();
    repaint();
  }

  public void restore() {
    getContentPane().remove(buttons);
    getContentPane().add(header,  BorderLayout.NORTH);
    getContentPane().add(main,    BorderLayout.CENTER);
    getContentPane().add(buttons, BorderLayout.SOUTH);
    setMinimumSize(new Dimension(442, 304));
    setMaximumSize(new Dimension(442, 304));
    setSize(442, 304);
    isCollapsed = false;
    revalidate();
    repaint();
  }

  public void setUpHeader(){
    header = new JPanel(new BorderLayout());
    header.setPreferredSize(new Dimension(442, 55));
    header.setOpaque(false);
    // header.setOpaque(true);
    // header.setBackground(Color.green);

    Border headerPadding = BorderFactory.createEmptyBorder(18, 28, 14, 28);
    header.setBorder(headerPadding);

    title = new JLabel();
    Font fTitle = new Font("Inter 18pt", Font.BOLD, 16);
    title.setText(session.getTitle());
    title.setFont(fTitle);
    title.setForeground(Theme.WHITE);

    Font fDate = new Font("Inter 18pt", Font.PLAIN, 15);

    JPanel dateWrapper = new JPanel(new GridBagLayout());
    dateWrapper.setOpaque(false);
    dateWrapper.setFont(fDate);
    datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    datePanel.setOpaque(false);

    startDate = new JLabel( Functions.formatTime( session.getStartDate() ) );
    startDate.setForeground(Theme.L_GREY);
    JLabel arrow = new JLabel("→");
    arrow.setForeground(Theme.L_GREY);
    endDate = new JLabel(); // This has to be the current time LocalDateTime.now() that keeps getting updated;
    endDate.setForeground(Theme.L_GREY);

    datePanel.add(startDate);
    datePanel.add(arrow);
    datePanel.add(endDate);

    dateWrapper.add(datePanel);

    header.add(title, BorderLayout.WEST);
    header.add(dateWrapper, BorderLayout.EAST);

    add(header, BorderLayout.NORTH);
  }

  public void setUpMain(){
    main = new JPanel(new GridBagLayout());
    main.setOpaque(false);

    totalTime = new JLabel("00:00");

    Font totalFont = new Font("BBH Bartle", Font.PLAIN , 39);
    totalTime.setFont(totalFont);
    totalTime.setForeground(Theme.LILA);

    splitTime = new JLabel("+ 00:00");
    splitTime.setForeground(Theme.LL_GREEN);

    Font splitTimeFont = new Font("Inter 24pt ExtraBold", Font.PLAIN, 30);
    splitTime.setFont(splitTimeFont);

    splitType = new JLabel(LangManager.get("split.study"));
    Font splitTypeFont = new Font("Inter 18pt", Font.BOLD, 13);
    splitType.setFont(splitTypeFont);
    splitType.setForeground(Theme.WHITE);

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridy = 0;
    gbc.insets = new Insets(0,0,0,0);

    main.add(totalTime, gbc);

    gbc.gridy = 1;
    gbc.insets = new Insets(0,0,0,0);

    main.add(splitTime, gbc);

    gbc.gridy = 2;
    gbc.insets = new Insets(0,0,0,0);

    main.add(splitType, gbc);

    add(main, BorderLayout.CENTER);
  }

  public void setUpButtons(){

    buttons = new JPanel(new BorderLayout());
    buttons.setOpaque(false);

    buttons.setPreferredSize(new Dimension(442, 124));
    Border buttonsPadding = BorderFactory.createEmptyBorder(8,55,14,55);

    buttons.setBorder(buttonsPadding);

    JPanel p1 = new JPanel(new GridBagLayout());
    p1.setOpaque(false);
    JPanel p2 = new JPanel(new GridBagLayout());
    p2.setOpaque(false);
    JPanel p3 = new JPanel(new GridBagLayout());
    p3.setOpaque(false);
    swap = new ChronoSwitchButton();
    collapse = new ChronoAddButton();
    stop = new ChronoStopButton();

    collapse.addActionListener(e -> {
      if(isCollapsed) restore();
      else collapse();
    }
    );

    swap.addActionListener(e -> {

      session.changeSplits();
      
      Split split = session.getCurrent();

      helpers.Type t = split.getType();
      String type = t == helpers.Type.STUDY ?
                    LangManager.get("split.study") : LangManager.get("split.pause");

      splitType.setText(type);
      splitTime.setForeground(split.getColor());
      splitTime.setText("+ 00:00");

    });

    stop.addActionListener(e -> {

      session.stop();
      dispose();

    });

    p1.add(swap);
    p2.add(collapse);
    p3.add(stop);

    buttons.add(p2, BorderLayout.WEST);
    buttons.add(p1, BorderLayout.CENTER);
    buttons.add(p3, BorderLayout.EAST);

    add(buttons, BorderLayout.SOUTH);

  }

  public void stopTicker() {
    if (ticker != null) ticker.stop();
  }

  private String fmt(Duration d) {
    long s = d.toSeconds();
    long h = s / 3600, m = (s % 3600) / 60, sec = s % 60;
    return h > 0
      ? String.format("%d:%02d:%02d", h, m, sec)
      : String.format("%02d:%02d", m, sec);
  }

}
