package ui.templates;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import ui.templates.HeaderComp.HeaderLogo;
import ui.templates.HeaderComp.HeaderMenu;
import utils.Theme;

public class Header extends JPanel{

  private Page page;
  private HeaderLogo logo;
  private HeaderMenu menu;
  private Border border;
  
  public Header( Page page , Runnable ToggleSidebar){

    this.page = page;

    logo = new HeaderLogo(this.page);
    menu = new HeaderMenu();
    menu.addActionListener( e -> ToggleSidebar.run() );

    border = BorderFactory.createCompoundBorder(
      BorderFactory.createMatteBorder(0, 0, 3, 0, Theme.WHITE),
      BorderFactory.createEmptyBorder(12,20,12,20)
    );

    setLayout(new BorderLayout());
    setBorder(border);
    add(logo, BorderLayout.WEST);
    add(menu, BorderLayout.EAST);
    setOpaque(false);
    setPreferredSize(new Dimension(1440,75));

  }

  public void setPage(Page page) {
    logo.setPage(page);
  }

}
