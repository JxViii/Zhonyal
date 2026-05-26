package ui.templates;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Locale;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import helpers.FileUtils;
import helpers.LangManager;
import helpers.UserProfile;
import ui.components.AppPopup;
import ui.templates.SidebarComp.LogOutItem;
import ui.templates.SidebarComp.SidebarItem;
import utils.Theme;

public class Sidebar extends JPanel {

  private SidebarItem home;
  private SidebarItem sessions;
  private SidebarItem notes;
  private JPanel linksWrapper;

  private JPanel logOutWrapper;
  private LogOutItem logOut;
  private SidebarItem profile;
  private JButton langToggle;
  private JLabel username;
  private Font usernameFont;

  private Border padding;
  private boolean isOpen = false;
  private Consumer<String> onNavigate;
  private Runnable onReset;
  private Runnable onProfile;

  public Sidebar(){

    setLayout(new BorderLayout());
    setOpaque(false);
    setPreferredSize(new Dimension(0,0));

    padding = BorderFactory.createEmptyBorder(18, 20, 20, 20);

    LinksSetUp();
    LogOutSetUp();

  }

  private void LinksSetUp(){

    linksWrapper = new JPanel();
    linksWrapper.setLayout(new BoxLayout(linksWrapper, BoxLayout.Y_AXIS));
    linksWrapper.setOpaque(false);

    home     = new SidebarItem(LangManager.get("sidebar.home"));
    sessions = new SidebarItem(LangManager.get("sidebar.sessions"));
    notes    = new SidebarItem(LangManager.get("sidebar.notes"));

    home.addActionListener(e -> fire("home"));
    sessions.addActionListener(e -> fire("sessions"));
    notes.addActionListener(e -> fire("notes"));

    linksWrapper.add(home);
    linksWrapper.add(Box.createVerticalStrut(10));
    linksWrapper.add(sessions);
    linksWrapper.add(Box.createVerticalStrut(10));
    linksWrapper.add(notes);
    linksWrapper.setBorder(padding);

    setActive("home");

    add(linksWrapper, BorderLayout.NORTH);
  }

  private void LogOutSetUp(){

    usernameFont = new Font("Inter 18pt", Font.BOLD, 13);

    logOutWrapper = new JPanel();
    logOutWrapper.setLayout(new BoxLayout(logOutWrapper, BoxLayout.Y_AXIS));
    logOutWrapper.setOpaque(false);

    logOut = new LogOutItem();
    logOut.setText(LangManager.get("sidebar.reset"));
    logOut.addActionListener(e ->
        AppPopup.warn(Sidebar.this, LangManager.get("dialog.reset.title"),
            LangManager.get("dialog.reset.body"),
            () -> {
              FileUtils.deleteAllFiles();
              if (onReset != null) onReset.run();
            })
    );

    langToggle = new JButton(LangManager.get("sidebar.lang"));
    langToggle.setFont(new Font("Inter 18pt", Font.BOLD, 13));
    langToggle.setForeground(Theme.WHITE);
    langToggle.setOpaque(false);
    langToggle.setContentAreaFilled(false);
    langToggle.setBorderPainted(true);
    langToggle.setFocusPainted(false);
    langToggle.setBorder(BorderFactory.createLineBorder(Theme.WHITE, 1));
    langToggle.setPreferredSize(new Dimension(60, 28));
    langToggle.setMaximumSize(new Dimension(60, 28));
    langToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    langToggle.addActionListener(e -> toggleLang());

    username = new JLabel( "@" + UserProfile.getName() );
    username.setFont(usernameFont);
    username.setForeground(Theme.L_GREY);

    profile = new SidebarItem(LangManager.get("sidebar.profile"));
    profile.addActionListener(e -> { if (onProfile != null) onProfile.run(); });

    logOutWrapper.add(username);
    logOutWrapper.add(Box.createVerticalStrut(10));
    logOutWrapper.add(profile);
    logOutWrapper.add(Box.createVerticalStrut(10));
    logOutWrapper.add(langToggle);
    logOutWrapper.add(Box.createVerticalStrut(10));
    logOutWrapper.add(logOut);
    logOutWrapper.setBorder(padding);

    this.add(logOutWrapper, BorderLayout.SOUTH);

  }

  private void toggleLang() {
    Locale next = LangManager.isEnglish() ? Locale.forLanguageTag("es-ES") : Locale.ENGLISH;
    LangManager.setLocale(next);
    refreshLabels();
  }

  public void refreshLabels() {
    home.setText(LangManager.get("sidebar.home"));
    sessions.setText(LangManager.get("sidebar.sessions"));
    notes.setText(LangManager.get("sidebar.notes"));
    logOut.setText(LangManager.get("sidebar.reset"));
    langToggle.setText(LangManager.get("sidebar.lang"));
    username.setText("@" + UserProfile.getName());
    profile.setText(LangManager.get("sidebar.profile"));
    home.repaint();
    sessions.repaint();
    notes.repaint();
    profile.repaint();
    logOut.repaint();
  }

  public void toggle(){

    if( !isOpen ){
      setPreferredSize(new Dimension(200,0));
      isOpen = true;
    }
    else{
      setPreferredSize(new Dimension(0,0));
      isOpen = false;
    }

    getParent().revalidate();
    getParent().repaint();
  }

  public void setOnNavigate(Consumer<String> handler) { this.onNavigate = handler; }
  public void setOnReset(Runnable r) { this.onReset = r; }
  public void setOnProfile(Runnable r) { this.onProfile = r; }

  public void setActive(String page) {
    home.setActive(page.equals("home"));
    sessions.setActive(page.equals("sessions"));
    notes.setActive(page.equals("notes"));
  }

  private void fire(String page) {
    if (onNavigate != null) onNavigate.accept(page);
  }

}
