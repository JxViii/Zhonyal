package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import helpers.LangManager;
import utils.Theme;

public class AppPopup extends JDialog {

    public enum Type { INFO, WARNING, CONFIRM }

    private static final Color ACCENT_INFO    = Theme.CYAN;
    private static final Color ACCENT_WARNING = new Color(0xF0A500);
    private static final Color ACCENT_CONFIRM = Theme.L_RED;
    private static final Color CARD_BG        = new Color(0x1C1C1C);
    private static final int   STRIP_H        = 7;

    private AppPopup(Window parent, Type type, String title, String message, Runnable onConfirm) {
        super(parent, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        Color accent = accentFor(type);

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                // g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, CORNER, CORNER));
                // g2.setColor(accent);
                // g2.fillRect(0, 0, w, STRIP_H);
                g2.setColor(CARD_BG);
                g2.fillRect(0, 0, w, h);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(STRIP_H + 20, 28, 24, 28));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 24));
        titleLabel.setForeground(Theme.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 12, 0));
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel msgLabel = new JLabel("<html><body style='width:340px; line-height:1.5'>" + message + "</body></html>");
        msgLabel.setFont(new Font("Inter 18pt", Font.PLAIN, 17));
        msgLabel.setForeground(Theme.LL_GREY);
        msgLabel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(20, 0, 0, 0));
        buttons.setAlignmentX(LEFT_ALIGNMENT);
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        if (type == Type.INFO) {
            JButton ok = makeBtn(LangManager.get("popup.ok"), true, accent);
            ok.addActionListener(e -> dispose());
            buttons.add(ok);
        } else {
            JButton cancel  = makeBtn(LangManager.get("popup.cancel"),  false, accent);
            JButton confirm = makeBtn(LangManager.get("popup.confirm"), true,  accent);
            cancel.addActionListener(e -> dispose());
            confirm.addActionListener(e -> { dispose(); if (onConfirm != null) onConfirm.run(); });
            buttons.add(cancel);
            buttons.add(confirm);
        }

        card.add(titleLabel);
        card.add(msgLabel);
        card.add(buttons);

        setContentPane(card);

        getRootPane().registerKeyboardAction(
            e -> dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        pack();
        if (getWidth() < 400) setSize(400, getHeight());
        setLocationRelativeTo(parent);
    }

    private JButton makeBtn(String text, boolean filled, Color accent) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(filled ? accent : new Color(0x2E2E2E));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        b.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 16));
        b.setForeground(filled ? Theme.WHITE : Theme.L_GREY);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setRolloverEnabled(false);
        b.setPreferredSize(new Dimension(128, 34));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static Color accentFor(Type type) {
        return switch (type) {
            case INFO    -> ACCENT_INFO;
            case WARNING -> ACCENT_WARNING;
            case CONFIRM -> ACCENT_CONFIRM;
        };
    }

    // ── Static factory methods ──────────────────────────────────────────────

    /** Cyan strip — one OK button. For validation errors and notices. */
    public static void info(Component source, String title, String message) {
        new AppPopup(SwingUtilities.getWindowAncestor(source), Type.INFO, title, message, null)
            .setVisible(true);
    }

    /** Yellow strip — Cancel + Confirm. For reversible or soft confirmations. */
    public static void warn(Component source, String title, String message, Runnable onConfirm) {
        new AppPopup(SwingUtilities.getWindowAncestor(source), Type.WARNING, title, message, onConfirm)
            .setVisible(true);
    }

    /** Red strip — Cancel + Confirm. For destructive actions (delete, reset). */
    public static void confirm(Component source, String title, String message, Runnable onConfirm) {
        new AppPopup(SwingUtilities.getWindowAncestor(source), Type.CONFIRM, title, message, onConfirm)
            .setVisible(true);
    }

}
