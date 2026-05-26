import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import utils.DB;
import utils.Fonts;

public class Main {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "lcd_hrgb");
        System.setProperty("swing.aatext", "true");
        DB.init();
        // DB.reset();
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                Fonts.load();
                new App();
            } catch (Exception e) {
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null,
                    e.getClass().getSimpleName() + ": " + e.getMessage(),
                    "Zhonyal failed to start",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
