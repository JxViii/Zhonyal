package ui.components.SessionsHomeComp;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import helpers.LangManager;
import utils.Theme;

public class SessionsHero extends JPanel{

  private JLabel title;
  private JLabel t1;
  private JLabel t2;
  private JLabel t3;

  private final Font titleFont = new Font("BBH Bartle", Font.PLAIN, 48);
  private final Font textFont = new Font("BBH Hegarty", Font.PLAIN, 48);

  public SessionsHero(){

    title = new JLabel(LangManager.get("sessions.hero.title"));
    title.setFont(titleFont);
    title.setForeground(Theme.WHITE);

    ImageIcon arrowIcon = new ImageIcon("images/arrow.png");
    Image arrowRx = arrowIcon.getImage().getScaledInstance(38, 150, Image.SCALE_SMOOTH);
    arrowIcon = new ImageIcon(arrowRx);

    JLabel arrow = new JLabel();
    arrow.setIcon(arrowIcon);
    arrow.setOpaque(false);

    t1 = new JLabel(LangManager.get("sessions.hero.t1"));
    t1.setFont(textFont);
    t1.setForeground(Theme.WHITE);

    t2 = new JLabel(LangManager.get("sessions.hero.t2"));
    t2.setFont(textFont);
    t2.setForeground(Theme.CYAN);

    t3 = new JLabel(LangManager.get("sessions.hero.t3"));
    t3.setFont(textFont);
    t3.setForeground(Theme.WHITE);

    JPanel textWrapper = new JPanel();
    textWrapper.setLayout(new GridBagLayout());
    textWrapper.setOpaque(false);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridy = 0; gbc.insets = new Insets(0,0,0,0);
    textWrapper.add(t1, gbc);
    gbc.gridy = 1; textWrapper.add(t2, gbc);
    gbc.gridy = 2; textWrapper.add(t3, gbc);

    setOpaque(false);
    setLayout(new FlowLayout(FlowLayout.LEFT, 80, 0));

    add(title);
    add(arrow);
    add(textWrapper);
  }

  public void refreshText() {
    title.setText(LangManager.get("sessions.hero.title"));
    t1.setText(LangManager.get("sessions.hero.t1"));
    t2.setText(LangManager.get("sessions.hero.t2"));
    t3.setText(LangManager.get("sessions.hero.t3"));
  }
}
