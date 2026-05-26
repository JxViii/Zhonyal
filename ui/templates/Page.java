package ui.templates;

import java.awt.Color;

import javax.swing.ImageIcon;

import utils.Theme;

public class Page {
  
  private String type;
  private Color headerColor;
  private ImageIcon logo;

  public Page(String type){

    if( !type.equals("Sessions") && !type.equals("Default"))
      throw new IllegalArgumentException("Invalid page type: " + type);
      
    this.type = type;
    if(getType().equals("Sessions")){
      this.headerColor = Theme.CYAN;
      this.logo = new ImageIcon("images/logo-sessions.png");
    }
    else{
      this.headerColor = Theme.L_GREEN;
      this.logo = new ImageIcon("images/logo-default.png");
    }
  }

  public String getType() { return this.type; }
  public Color getColor() { return this.headerColor; }
  public ImageIcon getLogo() { return this.logo; }

}
