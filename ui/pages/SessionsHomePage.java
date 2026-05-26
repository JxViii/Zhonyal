package ui.pages;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import helpers.LangManager;
import helpers.Session;
import ui.components.AppPopup;
import ui.components.SessionsHomeComp.SessionStartButton;
import ui.components.SessionsHomeComp.SessionsHero;
import ui.components.SessionsHomeComp.SessionsViewButton;

public class SessionsHomePage extends JPanel{

  private JPanel heroPanel;
  private JPanel buttonsPanel;
  private SessionsHero hero;
  
  private SessionStartButton start;
  private JPanel startPanel;
  private SessionsViewButton view;
  private JPanel viewPanel;

  private boolean isChrono;
  private Consumer<String> onNavigate;

  public void setOnNavigate(Consumer<String> handler) { this.onNavigate = handler; }
  public boolean isChronoRunning() { return isChrono; }

  public SessionsHomePage(){

    setLayout(new BorderLayout());
    setOpaque(false);

    isChrono = false;
    //hero 

    heroPanel = new JPanel(new GridBagLayout());
    hero = new SessionsHero();

    heroPanel.setOpaque(false);
    heroPanel.add(hero);

    add(heroPanel, BorderLayout.CENTER);

    // buttons

    buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridLayout(1,2));

    Border cPadding = BorderFactory.createEmptyBorder(80,40,80,40);

    startPanel = new JPanel(new GridBagLayout());
    startPanel.setOpaque(false);

    start = new SessionStartButton();
    start.addStartListener(e -> {

      if(isChrono) {
        AppPopup.info(SessionsHomePage.this, LangManager.get("sessions.inprogress.title"),
            LangManager.get("sessions.inprogress.body"));
        return;
      }

      String title = start.getSessionTitle();
      if (title.isBlank()) {
        AppPopup.info(SessionsHomePage.this, LangManager.get("sessions.notitle.title"),
            LangManager.get("sessions.notitle.body"));
        return;
      }
      Session session = new Session(title);
      session.start();
      Frame owner = (Frame) SwingUtilities.getWindowAncestor(SessionsHomePage.this);
      Chrono chrono = new Chrono(owner, session);
      chrono.setVisible(true);

      chrono.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosed(java.awt.event.WindowEvent e) {
          isChrono = false;
      }
      });

      isChrono = true;

    });

    startPanel.add(start);

    viewPanel = new JPanel(new GridBagLayout());
    viewPanel.setOpaque(false);

    view = new SessionsViewButton();
    view.addActionListener(e -> {
      if (onNavigate != null) onNavigate.accept("sessions-manager");
    });

    viewPanel.add(view);

    buttonsPanel.setOpaque(false);
    buttonsPanel.setBorder(cPadding);
    buttonsPanel.add(startPanel);
    buttonsPanel.add(viewPanel);

    add(buttonsPanel, BorderLayout.SOUTH);

  }

  public void refreshText() {
    hero.refreshText();
    start.refreshText();
    view.refreshText();
  }

}
