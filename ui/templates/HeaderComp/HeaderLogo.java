package ui.templates.HeaderComp;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.templates.Page;
import utils.Theme;

public class HeaderLogo extends JPanel {

    private JPanel hWrapper;
    private JLabel h1;
    private JLabel h2;
    private ImageIcon logo;
    private Color color;

    private JLabel image;

    public HeaderLogo(Page page) {
        this.color = page.getColor();
        this.logo = page.getLogo();

        h1 = new JLabel("Zhonyal");
        h2 = new JLabel("Zhonyal");

        Font font = new Font("BBH Bartle", Font.PLAIN, 20);
        h1.setFont(font);
        h1.setForeground(Theme.WHITE);
        h2.setFont(font);
        h2.setForeground(this.color);

        // Text wrapper with GridBagLayout for negative margin
        hWrapper = new JPanel(new GridBagLayout());
        hWrapper.setOpaque(false);
        hWrapper.setAlignmentY(CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0;
        gbc.insets = new Insets(3, 0, 0, 0);
        hWrapper.add(h1, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(-8, 0, 0, 0);  // pull up
        hWrapper.add(h2, gbc);

        // 'this' uses its own layout for logo + text side by side
        setLayout(new FlowLayout(
          FlowLayout.CENTER, 0, 0
        ));
        setOpaque(false);

        image = new JLabel();

        Image logoRX = logo.getImage().getScaledInstance(35, 49, Image.SCALE_SMOOTH);
        logo = new ImageIcon(logoRX);
        image.setIcon(logo);

        add(image);
        add(Box.createHorizontalStrut(25));
        add(hWrapper);
    }

    public void setPage(Page page) {
        h2.setForeground(page.getColor());
        Image logoRX = page.getLogo().getImage().getScaledInstance(35, 49, Image.SCALE_SMOOTH);
        image.setIcon(new ImageIcon(logoRX));
        repaint();
    }
}