package helpers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Session {
  
  private long id;
  private String title;
  private LocalDateTime start;
  private LocalDateTime end;
  private List<Split> splits;
  private boolean isRunning;
  private Integer n; // I used this to track number of splits to get their new titles
  private Type nextSplit; // Next Split tracker

  private Split currSplit;

  public long getId() { return id; }
  public void setId(long id) { this.id = id; }
  public String getTitle() { return title; }
  public LocalDateTime getStart() { return start; }
  public LocalDateTime getEnd() { return end; }
  public String getStartDate() { return start.toString(); }
  public String getEndDate() { return end.toString(); }
  public List<Split> getSplits() { return splits; }
  public Split getCurrent() { return currSplit; }

  public static Session fromDB(long id, String title, String start, String end, List<Split> splits) {
    Session s = new Session(title);
    s.id = id;
    s.start = LocalDateTime.parse(start);
    s.end = LocalDateTime.parse(end);
    s.splits = splits;
    s.n = splits.size();
    s.isRunning = false;
    return s;
  }

  public Session( String title){

    this.title = title;
    this.nextSplit = Type.STUDY;
    this.n = 0;
    this.splits = new ArrayList<>();

  }

  public void start() {
    
    this.start = LocalDateTime.now();
    this.isRunning = true;

    currSplit = new Split(++n, nextSplit);
    splits.add(currSplit);

  }

  public void stop() {

    if(!isRunning) return;

    currSplit.stop();
    end = LocalDateTime.now();
    System.out.println(get());

    isRunning = false;
    utils.DB.save(this);

  }

  public void changeSplits(){

    if(!isRunning) return;

    currSplit.stop();

    nextSplit = (nextSplit.equals(Type.STUDY)) ? Type.PAUSE : Type.STUDY;

    currSplit = new Split(++n, nextSplit);
    splits.add(currSplit);

  }

  public Duration getTotalTime(){
      LocalDateTime endTime = end != null ? end : LocalDateTime.now();
      return Duration.between(start, endTime);
  }

  public Duration getStudyTime(){

    Duration time = Duration.ZERO;

    for( Split split : splits ){
      if(split.getType().equals(Type.STUDY))
        time = time.plus(split.getDuration());
    }

    return time;
  }

  public Duration getPauseTime(){

    Duration time = Duration.ZERO;

    for( Split split : splits ){
      if(split.getType().equals(Type.PAUSE))
        time = time.plus(split.getDuration());
    }

    return time;
  }

  public double getFocusRate() {
      long total = getTotalTime().toSeconds();
      if (total == 0) return 0;
      return (double) getStudyTime().toSeconds() / total * 100;
  }

  public String get() {
      var fmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
      Duration total = getTotalTime();
      Duration study = getStudyTime();
      Duration pause = getPauseTime();

      String totalStr = String.format("%d:%02d:%02d", 
          total.toHours(), total.toMinutesPart(), total.toSecondsPart());
      String studyStr = String.format("%d:%02d:%02d", 
          study.toHours(), study.toMinutesPart(), study.toSecondsPart());
      String pauseStr = String.format("%d:%02d:%02d", 
          pause.toHours(), pause.toMinutesPart(), pause.toSecondsPart());

      StringBuilder sb = new StringBuilder();
      sb.append("=== SESSION: ").append(title).append(" ===\n");
      sb.append("Start: ").append(start.format(fmt)).append("\n");
      sb.append("End:   ").append(end != null ? end.format(fmt) : "running").append("\n");
      sb.append("Total: ").append(totalStr).append("\n");
      sb.append("Study: ").append(studyStr).append("\n");
      sb.append("Pause: ").append(pauseStr).append("\n");
      sb.append("Focus: ").append(String.format("%.1f%%", getFocusRate())).append("\n");
      sb.append("Splits: ").append(splits.size()).append("\n");
      for (Split s : splits) {
          sb.append("  ").append(s.get()).append("\n");
      }
      return sb.toString();
  }
}
