package ui.components.HomeComp;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import helpers.LangManager;
import helpers.UserProfile;
import utils.Theme;

public class HomeHero extends JPanel{
  
  private ImageIcon logo;
  private JLabel logoLabel;
  private JLabel title;
  private JLabel greetings;
  private JPanel textWrapper;
  private Font titleFont;
  private Font greetingsFont;

  public HomeHero(){

    titleFont = new Font("BBH Bartle", Font.PLAIN, 64);
    greetingsFont = new Font("Inter 18pt", Font.BOLD, 35);

    logo = new ImageIcon("images/HeroLogo.png");
    logoLabel = new JLabel();
    Image logoRx = logo.getImage().getScaledInstance(297, 352, Image.SCALE_SMOOTH);
    logo = new ImageIcon(logoRx);
    logoLabel.setIcon(logo);

    title = new JLabel("Zhonyal");
    title.setForeground(Theme.WHITE);
    title.setFont(titleFont);
    greetings = new JLabel(LangManager.get("home.greeting") + UserProfile.getName());
    greetings.setForeground(Theme.LL_GREEN);
    greetings.setFont(greetingsFont);
    textWrapper = new JPanel();
    textWrapper.setLayout(new GridBagLayout());
    textWrapper.setOpaque(false);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.WEST;

    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 0, 0);

    textWrapper.add(title, gbc);

    gbc.gridy = 1;
    gbc.insets = new Insets(10, 0, 0, 0);

    textWrapper.add(greetings, gbc);

    setLayout(new GridBagLayout());
    setOpaque(false);

    gbc.gridx = 0;
    gbc.insets = new Insets(0,0,0,0);

    add(logoLabel, gbc);

    gbc.gridx = 1;
    gbc.insets = new Insets(0,-35,0,0);

    add(textWrapper, gbc);

  }

  public void refresh() {
    greetings.setText(LangManager.get("home.greeting") + UserProfile.getName());
    repaint();
  }


}

/*
  TODO BUTTON TO CHANGE THE USER AND EMAIL
  TODO DOCUMENTATION
*/
