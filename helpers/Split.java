package helpers;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;

import utils.Theme;

public class Split {
  
  private Type type;
  private String title;
  private LocalDateTime start;
  private LocalDateTime end;

  public String getTitle() { return title; }
  public LocalDateTime getStart() { return start; }
  public LocalDateTime getEnd() { return end; }
  public String getStartDate() { return start.toString(); }
  public String getEndDate() { return end.toString(); }
  public Type getType() { return type; }

  public Split( Integer n , Type type){

    this.type = type;
    this.title = "Split #" + n;
    this.start = LocalDateTime.now();
    this.end = null;

  }

  public Duration getDuration() {

    //runs timer
    LocalDateTime endTime = end != null ? end : LocalDateTime.now();
    return Duration.between(start, endTime);

  }
  public Color getColor(){
    
    return type == Type.STUDY ? Theme.LL_GREEN : Theme.L_RED;

  }

  public static Split fromDB(String title, String type, String start, String end) {
    Split s = new Split(0, Type.valueOf(type));
    s.title = title;
    s.start = LocalDateTime.parse(start);
    s.end = LocalDateTime.parse(end);
    return s;
  }

  public void start(){}
  public void stop(){ end = LocalDateTime.now(); }

  public String get() {
      Duration d = getDuration();
      String time = String.format("%d:%02d:%02d", 
          d.toHours(), d.toMinutesPart(), d.toSecondsPart());
      
      String startStr = start.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
      String endStr = end != null 
          ? end.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) 
          : "running";

      return String.format("[%s] %s | %s | %s → %s", 
          type, title, time, startStr, endStr);
      // Example: [STUDY] Split #0 | 1:32:46 | 18:26 → 20:00
  }
}
