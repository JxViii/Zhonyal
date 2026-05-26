package ui.pages;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import helpers.FilterState;
import helpers.LangManager;
import helpers.Session;
import ui.components.AppPopup;
import ui.components.SessionsManagerComp.FilterBar;
import ui.components.SessionsManagerComp.SessionItem;
import utils.DB;
import utils.Theme;

public class SessionsManager extends JPanel{

  private JPanel heroPanel;
  private JLabel title;
  private JLabel desc;

  private FilterBar filterPanel;
  private JPanel sessionsPanel;
  private JPanel grid;

  private List<Session> sessions = DB.loadAll();

  private boolean delete = false;

  private List<SessionItem> sessionItems;
  private Consumer<Session> onSessionSelect;

  public void setOnSessionSelect(Consumer<Session> handler) { this.onSessionSelect = handler; }

  public SessionsManager(){

    setLayout(new BorderLayout());
    setOpaque(false);

    setUpHero();

    setUpFilter();

    //grid

    grid = new JPanel();
    grid.setOpaque(false);
    grid.setLayout(new BoxLayout(grid, BoxLayout.Y_AXIS));
    grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    refresh();

    JScrollPane scroll = new JScrollPane(grid);
    scroll.setOpaque(false);
    scroll.getViewport().setOpaque(false);
    scroll.setBorder(null);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    scroll.setPreferredSize(new Dimension(1040, 450));

    scroll.addMouseWheelListener(e -> {
      JScrollBar bar = scroll.getVerticalScrollBar();
      bar.setValue(bar.getValue() + e.getUnitsToScroll() * 10);
    });

    sessionsPanel = new JPanel( new GridBagLayout() );
    sessionsPanel.setOpaque(false);

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridy=0;
    gbc.insets = new Insets(-30,0,0,0);
    
    sessionsPanel.add(scroll, gbc);

    add(sessionsPanel, BorderLayout.CENTER);

    JPanel deletePanel = new JPanel(new FlowLayout( FlowLayout.TRAILING, 20, 0 ));
    deletePanel.setOpaque(false);
    deletePanel.setPreferredSize(new Dimension(1050,40));

    Font delFont = new Font("Inter 18pt", Font.BOLD, 15);

    JButton deleteButton = new JButton(LangManager.get("sessions.manager.delete"));
    deleteButton.setOpaque(false);
    deleteButton.setContentAreaFilled(false);
    deleteButton.setBorderPainted(false);
    deleteButton.setFocusPainted(false);
    deleteButton.setPreferredSize(new Dimension(150, 36));
    deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    deleteButton.setFont(delFont);
    deleteButton.setBorder(
      BorderFactory.createLineBorder(Theme.WHITE, 2)
    );
    deleteButton.setForeground(Theme.WHITE);
    deleteButton.addActionListener(e -> {

      delete = !delete;
      
      deleteButton.setForeground(delete ? Theme.LL_RED : Theme.WHITE);


      refresh();

    });

    deletePanel.add(deleteButton);
    
    gbc.gridy=1;
    gbc.insets = new Insets(20,0,0,0);

    sessionsPanel.add(deletePanel, gbc);

    add(sessionsPanel, BorderLayout.CENTER);

  }

  public void setUpHero(){

    heroPanel = new JPanel();
    heroPanel.setLayout(new GridBagLayout());
    heroPanel.setOpaque(false);

    Font titleFont = new Font("BBH Bartle", Font.PLAIN, 48);
    Font descFont = new Font("Inter 18pt", Font.BOLD, 24);

    title = new JLabel(LangManager.get("sessions.manager.title"));
    title.setFont(titleFont);
    title.setForeground(Theme.WHITE);
    title.setHorizontalAlignment(JLabel.CENTER);

    desc = new JLabel(LangManager.get("sessions.manager.desc"));
    desc.setFont(descFont);
    desc.setForeground(Theme.L_GREY);
    
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(0, 0, 0, 0);
    
    heroPanel.add(title, gbc);

    gbc.gridy = 1;
    gbc.insets = new Insets(20, 0, 0, 0);

    heroPanel.add(desc, gbc);

    Border heroPadding = BorderFactory.createEmptyBorder(40,10,25,10);
    heroPanel.setBorder(heroPadding);

  }

  public void setUpFilter(){

    filterPanel = new FilterBar( () -> refresh() );

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridy = 2;
    gbc.insets = new Insets(50, 0, 0, 0);

    heroPanel.add(filterPanel, gbc);

    add(heroPanel, BorderLayout.NORTH);
  }

  public void refresh(){
    grid.removeAll();

    sessions = DB.loadAll();

    FilterState state = new FilterState(
      filterPanel.getActiveFilter(),
      filterPanel.getDateFrom(),
      filterPanel.getDateTo(),
      sessions
    );

    sessionItems = state.filter().stream()
                  .map(s -> new SessionItem(s, delete,
                      sess -> { if (onSessionSelect != null) onSessionSelect.accept(sess); },
                      () -> AppPopup.confirm(SessionsManager.this, LangManager.get("sessions.delete.title"),
                              LangManager.format("sessions.delete.body", s.getTitle()),
                              () -> {
                                DB.delete(s.getId()); sessions.remove(s); refresh();
                              })
                  ))
                  .toList();

    for( SessionItem s : sessionItems ){
      grid.add(s);
      grid.add(Box.createVerticalStrut(20));
    }

    revalidate();
    repaint();
  }

}
