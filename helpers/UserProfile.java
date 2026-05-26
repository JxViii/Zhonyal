package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UserProfile {

    private static final File FILE = new File(
        System.getProperty("user.home") + "/.zhonyal/user.properties"
    );

    public static boolean exists() {
        return FILE.exists();
    }

    public static void save(String name, String email) {
        FILE.getParentFile().mkdirs();
        Properties p = new Properties();
        p.setProperty("name", name);
        p.setProperty("email", email);
        try (FileOutputStream out = new FileOutputStream(FILE)) {
            p.store(out, null);
            System.out.println("Stored: " + p.getProperty("name") + p.getProperty("email"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getName() {
        return load().getProperty("name", "");
    }

    public static String getEmail() {
        return load().getProperty("email", "");
    }

    private static Properties load() {
        Properties p = new Properties();
        if (FILE.exists()) {
            try (FileInputStream in = new FileInputStream(FILE)) {
                p.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }
}
