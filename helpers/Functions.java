package helpers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Functions {

  public static String formatTime(String dateStr) {
    LocalDateTime dt = LocalDateTime.parse(dateStr);
    return dt.format(DateTimeFormatter.ofPattern("HH:mm"));
  }

  public static String formatDate(String dateStr) {
    LocalDateTime dt = LocalDateTime.parse(dateStr);
    return dt.format(DateTimeFormatter.ofPattern("d MMMM y"));
  }

  public static String formatDateTime(String dateStr) {
    LocalDateTime dt = LocalDateTime.parse(dateStr);
    return dt.format(DateTimeFormatter.ofPattern("d MMM HH:mm"));
  }

  public static String formatDuration(Duration d) {
    long s = d.toSeconds();
    long h = s / 3600, m = (s % 3600) / 60, sec = s % 60;
    return h > 0
      ? String.format("%d:%02d:%02d", h, m, sec)
      : String.format("%02d:%02d", m, sec);
  }

}
