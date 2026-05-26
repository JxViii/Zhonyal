package ui.pages;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import helpers.LangManager;
import helpers.UserProfile;
import utils.Theme;

public class OnboardingPage extends JPanel {

    private Runnable onComplete;
    private JTextField nameField;
    private JTextField emailField;
    private JLabel errorLabel;
    private boolean showError = false;

    private final Image greenBg = new ImageIcon("images/green-bg.png").getImage();


    public void setOnComplete(Runnable r) { this.onComplete = r; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(greenBg, 0, 0, getWidth(), getHeight(), this);
    }

    public OnboardingPage() {
        setLayout(new GridBagLayout());
        setOpaque(true);
        add(buildContent(), new GridBagConstraints());
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);

        // ── Logo row ─────────────────────────────────────────────────────────
        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        logoRow.setOpaque(false);
        logoRow.setAlignmentX(CENTER_ALIGNMENT);
        logoRow.setMaximumSize(new Dimension(500, 80));

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel appName = new JLabel("ZHONYAL");
        appName.setFont(new Font("BBH Bartle", Font.PLAIN, 65));
        appName.setForeground(Theme.WHITE);

        JLabel subtitle = new JLabel(LangManager.get("onboarding.subtitle"));
        subtitle.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 27));
        subtitle.setForeground(Theme.L_GREEN);

        titleBlock.add(appName);
        titleBlock.add(Box.createVerticalStrut(2));
        titleBlock.add(subtitle);

        logoRow.add(buildIcon());
        logoRow.add(titleBlock);

        // ── Fields ───────────────────────────────────────────────────────────
        nameField  = makeTextField(LangManager.get("onboarding.name.hint"));
        emailField = makeTextField(LangManager.get("onboarding.email.hint"));

        // ── Error label ──────────────────────────────────────────────────────
        errorLabel = new JLabel(LangManager.get("onboarding.error"));
        errorLabel.setFont(new Font("Inter 18pt", Font.PLAIN, 13));
        errorLabel.setForeground(Theme.L_RED);
        errorLabel.setVisible(false);

        // ── Button ───────────────────────────────────────────────────────────
        JButton createBtn = buildButton(LangManager.get("onboarding.button"));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx  = 0;
        c.fill   = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;

        c.gridy = 0; c.insets = new Insets(0, 0, 0, 0);
        content.add(logoRow, c);

        c.gridy = 1; c.insets = new Insets(36, 0, 0, 0);
        content.add(makeFieldBlock(LangManager.get("onboarding.name.label"), nameField), c);

        c.gridy = 2; c.insets = new Insets(16, 0, 0, 0);
        content.add(makeFieldBlock(LangManager.get("onboarding.email.label"), emailField), c);

        c.gridy = 3; c.insets = new Insets(10, 0, 0, 0);
        content.add(errorLabel, c);

        c.gridy = 4; c.insets = new Insets(50, 0, 0, 0);
        content.add(createBtn, c);

        return content;
    }

    private JPanel buildIcon() {
        JPanel icon = new JPanel();
        ImageIcon logo = new ImageIcon("images/logo-2.png");
        Image logoRX = logo.getImage().getScaledInstance(131, 189, Image.SCALE_SMOOTH);
        logo = new ImageIcon(logoRX);

        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(logo);

        icon.add(iconLabel);
        icon.setOpaque(false);
        return icon;
    }

    private JTextField makeTextField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setFont(new Font("Inter 18pt", Font.PLAIN, 17));
        f.setForeground(Theme.LL_GREY);
        f.setCaretColor(Theme.WHITE);
        f.setOpaque(false);
        f.setPreferredSize(new Dimension(528, 56));
        f.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(Theme.WHITE); }
            }
            @Override public void focusLost(FocusEvent e) {
                if (f.getText().isBlank()) { f.setText(placeholder); f.setForeground(Theme.LL_GREY); }
            }
        });
        return f;
    }

    private JPanel makeFieldBlock(String labelText, JTextField field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Inter 18pt", Font.PLAIN, 17));
        lbl.setForeground(Theme.LL_GREY);
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        JPanel box = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                g2.setColor(showError ? Theme.L_RED : Theme.L_GREY);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
                g2.dispose();
            }
        };
        box.setOpaque(false);
        box.setPreferredSize(new Dimension(528, 43));
        box.setAlignmentX(LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        box.add(field, gbc);

        p.add(lbl);
        p.add(Box.createVerticalStrut(6));
        p.add(box);
        return p;
    }

    private JButton buildButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(Theme.L_GREEN);
                g2.fillRoundRect(0, 0, w, h, 24, 24);
                g2.setColor(Theme.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (w - fm.stringWidth(getText())) / 2;
                int y = (h + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 16));
        btn.setForeground(new Color(0x111111));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setPreferredSize(new Dimension(440, 48));
        btn.setMaximumSize(new Dimension(440, 48));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> submit());
        return btn;
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        regexPattern = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@" + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
        .matcher(emailAddress)
        .matches();
    }

    private void submit() {
        String name  = fieldValue(nameField,  LangManager.get("onboarding.name.hint"));
        String email = fieldValue(emailField, LangManager.get("onboarding.email.hint"));

        if (name.isBlank() || email.isBlank()) {
            showError = true;
            errorLabel.setText(LangManager.get("onboarding.error"));
            errorLabel.setVisible(true);
            repaint();
            return;
        }
        else if( !patternMatches(email, "") ){
            showError = true;
            errorLabel.setText(LangManager.get("gmail.error"));
            errorLabel.setVisible(true);
            repaint();
            return;
        }

        showError = false;
        errorLabel.setVisible(false);
        UserProfile.save(name, email);
        if (onComplete != null) onComplete.run();
    }

    private String fieldValue(JTextField f, String placeholder) {
        String t = f.getText();
        return t.equals(placeholder) ? "" : t.trim();
    }
}
