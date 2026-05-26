package utils;

public class Design {

  public static String addLetterSpacing(String text) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < text.length(); i++) {
        sb.append(text.charAt(i));
        if (i < text.length() - 1) {
            sb.append("&#8202;"); // one space = subtle spacing
        }
    }
    return sb.toString();
  }
  
}
