package helpers;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class FilterState {
  
  private String order;
  private LocalDate start;
  private LocalDate end;
  private List<Session> sessions;

  public FilterState(
    String order,
    LocalDate start,
    LocalDate end,
    List<Session> sessions
  ){

    this.order = order;
    this.start = start;
    this.end = end;
    this.sessions = sessions;

  }

  public List<Session> filter(){

    List<Session> filtered = sessions.stream()
                            .sorted( 
                              (order.equals("newest")) ?
                              Comparator.comparing(Session::getStartDate).reversed() :
                              Comparator.comparing(Session::getStartDate)
                             )
                            .filter( s -> {
                              if(start == null) return true;
                              LocalDate sessionStart = s.getStart().toLocalDate();
                              return !sessionStart.isBefore(start);
                              }
                            )
                            .filter( s -> {
                              if(end == null) return true;
                              LocalDate sessionEnd = s.getEnd().toLocalDate();
                              return !sessionEnd.isAfter(end);
                              }
                            )
                            .toList();
                            
    return filtered;

  }

}
