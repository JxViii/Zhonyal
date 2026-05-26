package ui.pages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import helpers.LangManager;
import ui.components.HomeComp.HomeCard;
import ui.components.HomeComp.HomeHero;

public class HomePage extends JPanel {

  private JPanel cardsPanel;
  private HomeCard sessions;
  private HomeCard notes;

  private HomeHero hero;
  private JPanel heroPanel;

  private Consumer<String> onNavigate;

  public void setOnNavigate(Consumer<String> handler) { this.onNavigate = handler; }

  public HomePage() {

    setOpaque(false);
    setLayout(new BorderLayout());
    // Hero

    hero = new HomeHero();
    heroPanel = new JPanel(new GridBagLayout());
    heroPanel.setOpaque(false);
    heroPanel.setPreferredSize(new Dimension(820,352));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.insets = new Insets(0, -100, 0, 0);

    heroPanel.add(hero, gbc);

    add(heroPanel, BorderLayout.CENTER);

    // Cards

    cardsPanel = new JPanel();
    cardsPanel.setLayout(new GridLayout(1,2));

    Border cPadding = BorderFactory.createEmptyBorder(80,40,80,40);

    sessions = new HomeCard(
      LangManager.get("home.sessions.title"),
      LangManager.get("home.sessions.desc"),
      "Sessions"
    );

    JPanel sessionWrapper = new JPanel();
    sessionWrapper.setLayout(new GridBagLayout());
    sessionWrapper.setOpaque(false);
    sessionWrapper.add(sessions);

    notes = new HomeCard(
      LangManager.get("home.notes.title"),
      LangManager.get("home.notes.desc"),
      "Notes"
    );

    JPanel notesWrapper = new JPanel();
    notesWrapper.setLayout(new GridBagLayout());
    notesWrapper.setOpaque(false);
    notesWrapper.add(notes);

    cardsPanel.setOpaque(false);

    sessions.addActionListener(e -> {
      if (onNavigate != null) onNavigate.accept("sessions");
    });

    notes.addActionListener(e -> {
      if (onNavigate != null) onNavigate.accept("notes");
    });

    cardsPanel.add(sessionWrapper);
    cardsPanel.add(notesWrapper);

    cardsPanel.setBorder(cPadding);
    cardsPanel.setMaximumSize(new Dimension(1240,557));

    this.add(cardsPanel, BorderLayout.SOUTH);
  }

  public void refresh() {
    sessions.setTitle(LangManager.get("home.sessions.title"));
    sessions.setDesc(LangManager.get("home.sessions.desc"));
    notes.setTitle(LangManager.get("home.notes.title"));
    notes.setDesc(LangManager.get("home.notes.desc"));
    hero.refresh();
    repaint();
  }

}
