package utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class Fonts{

  public static void load(){

    try{
      Font BBH_Bartle = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/BBH_Bartle/BBHBartle-Regular.ttf"));
      Font BBH_Hegarty = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/BBH_Hegarty/BBHHegarty-Regular.ttf"));
      Font Inter = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Inter/static/Inter-Regular.ttf"));
      Font Inter_Bold = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Inter/static/Inter_24pt-ExtraBold.ttf"));
      Font GochiHand = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Gochi_Hand/GochiHand-Regular.ttf"));

      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

      ge.registerFont(Inter);
      ge.registerFont(Inter_Bold);
      ge.registerFont(BBH_Bartle);
      ge.registerFont(BBH_Hegarty);
      ge.registerFont(GochiHand);

      for( String f : ge.getAvailableFontFamilyNames()) {
        if(f.startsWith("Inter")) System.out.println(f);
      }
      
    } catch (FontFormatException | IOException e){
      e.printStackTrace();
    }

  }

}