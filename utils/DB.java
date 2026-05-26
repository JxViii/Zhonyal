package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import helpers.Session;
import helpers.Split;

public class DB {

    private static final String URL = buildUrl();

    private static String buildUrl() {
        if (System.getProperty("os.name", "").toLowerCase().contains("win")) {
            String base = System.getenv("LOCALAPPDATA");
            if (base == null) base = System.getProperty("user.home") + "\\AppData\\Local";
            java.io.File dir = new java.io.File(base, "Zhonyal");
            dir.mkdirs();
            return "jdbc:sqlite:" + new java.io.File(dir, "zhonyal.db").getAbsolutePath();
        }
        return "jdbc:sqlite:zhonyal.db";
    }
    private static Connection conn;

    public static void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(URL);
            createTables();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void createTables() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS sessions (" +
                "  id    INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  title TEXT    NOT NULL," +
                "  start TEXT    NOT NULL," +
                "  end   TEXT    NOT NULL" +
                ")"
            );
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS splits (" +
                "  id         INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  session_id INTEGER NOT NULL," +
                "  title      TEXT    NOT NULL," +
                "  type       TEXT    NOT NULL," +
                "  start      TEXT    NOT NULL," +
                "  end        TEXT    NOT NULL," +
                "  FOREIGN KEY (session_id) REFERENCES sessions(id)" +
                ")"
            );
        }
    }

    public static void reset() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS splits");
            st.executeUpdate("DROP TABLE IF EXISTS sessions");
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void backup(String destPath) {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("VACUUM INTO '" + destPath.replace("'", "''") + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void save(Session session) {
        String insertSession =
            "INSERT INTO sessions (title, start, end) VALUES (?, ?, ?)";
        String insertSplit =
            "INSERT INTO splits (session_id, title, type, start, end) VALUES (?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            long sessionId;
            try (PreparedStatement ps = conn.prepareStatement(
                    insertSession, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, session.getTitle());
                ps.setString(2, session.getStartDate());
                ps.setString(3, session.getEndDate());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    sessionId = keys.getLong(1);
                }
            }

            session.setId(sessionId);

            try (PreparedStatement ps = conn.prepareStatement(insertSplit)) {
                for (Split split : session.getSplits()) {
                    ps.setLong(1, sessionId);
                    ps.setString(2, split.getTitle());
                    ps.setString(3, split.getType().name());
                    ps.setString(4, split.getStartDate());
                    ps.setString(5, split.getEndDate());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            javax.swing.JOptionPane.showMessageDialog(null,
                "Session could not be saved: " + e.getClass().getSimpleName() + ": " + e.getMessage(),
                "Zhonyal", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            try { conn.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public static List<Session> loadAll() {
        List<Session> sessions = new ArrayList<>();
        String querySessions = "SELECT id, title, start, end FROM sessions ORDER BY start DESC";
        String querySplits   = "SELECT title, type, start, end FROM splits WHERE session_id = ? ORDER BY start";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(querySessions)) {
            while (rs.next()) {
                long   id    = rs.getLong("id");
                String title = rs.getString("title");
                String start = rs.getString("start");
                String end   = rs.getString("end");

                List<Split> splits = new ArrayList<>();
                try (PreparedStatement ps = conn.prepareStatement(querySplits)) {
                    ps.setLong(1, id);
                    try (ResultSet sr = ps.executeQuery()) {
                        while (sr.next()) {
                            splits.add(Split.fromDB(
                                sr.getString("title"),
                                sr.getString("type"),
                                sr.getString("start"),
                                sr.getString("end")
                            ));
                        }
                    }
                }
                sessions.add(Session.fromDB(id, title, start, end, splits));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public static void delete(long id) {
        String deleteSplits  = "DELETE FROM splits  WHERE session_id = ?";
        String deleteSession = "DELETE FROM sessions WHERE id = ?";

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(deleteSplits)) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(deleteSession)) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
