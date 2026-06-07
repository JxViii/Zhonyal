package ui.pages;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
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
  private JPanel elements;
  private JPanel background;
  private final ImageIcon catImg = new ImageIcon("images/gif240.gif");
  private JPanel catPanel;
  private JLabel cat;

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
  private Boolean isCat = true;
  private Boolean isCatHovered = false;

  public Chrono(Frame owner, Session session, boolean isCat_) {

    super(owner);

    this.isCat = isCat_;
    this.session = session;
    isCollapsed = false;

    setSize(442, 304);
    setLocationRelativeTo(null);

    setAlwaysOnTop(true);
    setShape(new RoundRectangle2D.Double(0, 0, 442, 304, 30, 30));

    JLayeredPane canvas = new JLayeredPane();
    Dimension d = new Dimension(442,304);
    canvas.setOpaque(false);

    background = new JPanel(new BorderLayout()) {
      @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Theme.LL_GREY);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);
        g2.dispose();
      }
    };

    background.setPreferredSize(d);
    background.setOpaque(true);
    background.setBackground(Theme.BLACK);
    background.setBounds(0, 0, 442, 304);

    elements = new JPanel(new BorderLayout());

    elements.setBounds(0, 0, 442, 304);
    elements.setPreferredSize(d);
    elements.setOpaque(false);

    if(isCat){
      createCat();
      canvas.add(catPanel, JLayeredPane.PALETTE_LAYER);
    }

    canvas.add(background, JLayeredPane.DEFAULT_LAYER);
    canvas.add(elements, JLayeredPane.MODAL_LAYER);

    setContentPane(canvas);

    // If I don't put this it doesn drag at all

    final int[] offset = new int[2];

    canvas.addMouseListener(new MouseAdapter() {
        @Override public void mousePressed(MouseEvent e) {
            if(!isCatHovered){
              offset[0] = e.getX();
              offset[1] = e.getY();
            }
        }
    });

    canvas.addMouseMotionListener(new MouseMotionAdapter() {
        @Override public void mouseDragged(MouseEvent e) {
          if(!isCatHovered)
            setLocation(e.getXOnScreen() - offset[0], e.getYOnScreen() - offset[1]);
        }
    });

    //header
    setUpHeader();

    //main
    setUpMain();

    //buttons
    setUpButtons();

    ticker = new Timer(1000, e -> {
      totalTime.setText(fmt(session.getTotalTime()));
      splitTime.setText("+ " + fmt(session.getCurrent().getDuration()));
      endDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
    });
    ticker.start();

  }


  public void createCat(){
    catPanel = new JPanel();
    catPanel.setOpaque(false);
    catPanel.setPreferredSize(new Dimension(442,304));
    catPanel.setBounds(180,0,315,315);
    cat = new JLabel();
    cat.setIcon(catImg);

    catPanel.add(cat);

    final int[] catoffset = new int[2];

    cat.addMouseListener(new MouseAdapter() {
        @Override public void mouseEntered(MouseEvent e) {
            isCatHovered = true;
            System.out.println("ENTER CAT");
        }

        @Override public void mouseExited(MouseEvent e) {
            isCatHovered = false;
            System.out.println("out CAT");
        }
    });

    cat.addMouseListener(new MouseAdapter() {
        @Override public void mousePressed(MouseEvent e) {
            catoffset[0] = e.getX();
            catoffset[1] = e.getY();
        }
    });

    cat.addMouseMotionListener(new MouseMotionAdapter() {
        @Override public void mouseDragged(MouseEvent e) {
          int newX = catPanel.getX() + e.getX() - catoffset[0];
          int newY = catPanel.getY() + e.getY() - catoffset[1];
          catPanel.setLocation(newX, newY);
        }
    });
  }

  public void collapse() {
    elements.remove(header);
    elements.remove(main);
    elements.remove(buttons);
    catPanel.setVisible(false);
    elements.add(buttons, BorderLayout.NORTH);
    background.setBounds(0, 0, 442, 124);
    elements.setBounds(0, 0, 442, 124);
    setMinimumSize(new Dimension(442, 124));
    setMaximumSize(new Dimension(442, 124));
    setSize(442, 124);
    setShape(new RoundRectangle2D.Double(0, 0, 442, 124, 30, 30));
    isCollapsed = true;
    revalidate();
    repaint();
  }

  public void restore() {
    elements.remove(buttons);
    elements.add(header,  BorderLayout.NORTH);
    elements.add(main,    BorderLayout.CENTER);
    elements.add(buttons, BorderLayout.SOUTH);
    catPanel.setVisible(true);
    background.setBounds(0, 0, 442, 304);
    elements.setBounds(0, 0, 442, 304);
    setMinimumSize(new Dimension(442, 304));
    setMaximumSize(new Dimension(442, 304));
    setSize(442, 304);
    setShape(new RoundRectangle2D.Double(0, 0, 442, 304, 30, 30));
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

    elements.add(header, BorderLayout.NORTH);
  }

  public void setUpMain(){
    main = new JPanel(new GridBagLayout());
    main.setOpaque(false);

    totalTime = new JLabel("00:00");

    Font totalFont = new Font("BBH Bartle", Font.PLAIN , 39);
    totalTime.setFont(totalFont);
    totalTime.setForeground(
      !isCat ? Theme.LILA : Theme.cat_LILA
     );

    splitTime = new JLabel("+ 00:00");
    splitTime.setForeground(
      !isCat ? Theme.LL_GREEN : Theme.cat_GREEN
    );

    Font splitTimeFont = new Font("Inter 24pt ExtraBold", Font.PLAIN, 30);
    splitTime.setFont(splitTimeFont);

    splitType = new JLabel(LangManager.get("split.study"));
    Font splitTypeFont = new Font("Inter 24pt ExtraBold", Font.BOLD, 15);
    splitType.setFont(splitTypeFont);
    splitType.setForeground(Theme.WHITE);

    GridBagConstraints gbc = new GridBagConstraints();

    int catTitle = 0, catSub = 0, catType = 0;

    if(isCat){
      catTitle = -140; catSub = -100; catType = -70;
    }

    gbc.anchor = isCat ? GridBagConstraints.WEST : GridBagConstraints.CENTER;
    gbc.gridy = 0;
    gbc.insets = new Insets(0,catTitle,0,0);

    main.add(totalTime, gbc);

    gbc.gridy = 1;
    gbc.insets = new Insets(0,catSub,0,0);

    main.add(splitTime, gbc);

    gbc.gridy = 2;
    gbc.insets = new Insets(0,catType,0,0);

    main.add(splitType, gbc);

    elements.add(main, BorderLayout.CENTER);
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
      splitTime.setForeground(split.getColor(isCat));
      splitTime.setText("+ 00:00");

    });

    stop.addActionListener(e -> {

      ticker.stop();
      session.stop();
      dispose();

    });

    p1.add(swap);
    p2.add(collapse);
    p3.add(stop);

    buttons.add(p2, BorderLayout.WEST);
    buttons.add(p1, BorderLayout.CENTER);
    buttons.add(p3, BorderLayout.EAST);

    elements.add(buttons, BorderLayout.SOUTH);

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
