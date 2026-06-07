package ui.components.SessionsHomeComp;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import helpers.LangManager;
import utils.Theme;

public class SessionStartButton extends JPanel {

    private JTextField titleInput;
    private JPanel plusBtn;

    private JLabel startLabel;
    private JLabel newLabel;
    private JLabel sessionLabel;
    private JLabel titleFieldLabel;
    private JLabel timeLabel;
    private JPanel field;

    private JPanel catBtn;
    private final ImageIcon catIcon = new ImageIcon("images/pets(1).png");
    private final ImageIcon catIconHover = new ImageIcon("images/pets(2).png");
    private JLabel catLabel;
    private boolean isCatSelected = false;

    public SessionStartButton() {
        setLayout(new BorderLayout());
        setOpaque(false);
        Dimension size = new Dimension(422, 317);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setBorder(new EmptyBorder(37, 55, 37, 55));

        add(buildTitleBlock(), BorderLayout.NORTH);
        add(buildTitleField(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);
    }

    public void clicked(){

        newLabel.setForeground(Theme.cat_GREEN);
        field.setBackground(Theme.cat_GREEN);
        catLabel.setIcon(catIconHover);
        revalidate();
        repaint();

    }

    public void dfl(){

        newLabel.setForeground(Theme.CYAN);
        field.setBackground(Theme.CYAN);
        catLabel.setIcon(catIcon);
        revalidate();
        repaint();
        
    }

    private JPanel buildTitleBlock() {
        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setOpaque(false);

        startLabel = new JLabel(LangManager.get("sessions.start.label"));
        startLabel.setFont(new Font("BBH Bartle", Font.PLAIN, 26));
        startLabel.setForeground(Theme.WHITE);
        startLabel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);

        newLabel = new JLabel(LangManager.get("sessions.start.new"));
        newLabel.setFont(new Font("Gochi Hand", Font.PLAIN, 26));
        newLabel.setForeground(Theme.CYAN);

        sessionLabel = new JLabel(LangManager.get("sessions.start.session"));
        sessionLabel.setFont(new Font("BBH Bartle", Font.BOLD, 26));
        sessionLabel.setForeground(Theme.WHITE);

        row.add(newLabel);
        row.add(sessionLabel);

        block.add(startLabel);
        block.add(row);
        return block;
    }

    private JPanel buildTitleField() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 0, 24, 0));

        field = new JPanel(new BorderLayout(10, 0));
        field.setBackground(isCatSelected ? Theme.cat_GREEN : Theme.CYAN);
        field.setBorder(new EmptyBorder(9, 18, 9, 18));

        titleFieldLabel = new JLabel(LangManager.get("sessions.start.titlefield"));
        titleFieldLabel.setFont(new Font("BBH Hegarty", Font.PLAIN, 17));
        titleFieldLabel.setForeground(Color.BLACK);

        String placeholder = LangManager.get("sessions.start.placeholder");
        titleInput = new JTextField();
        titleInput.setFont(new Font("Inter 18pt", Font.PLAIN, 17));
        titleInput.setForeground(new Color(0x111111));
        titleInput.setBackground(Theme.CYAN);
        titleInput.setBorder(null);
        titleInput.setOpaque(false);
        titleInput.setText(placeholder);
        titleInput.setForeground(new Color(0x333333));

        titleInput.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (titleInput.getText().equals(LangManager.get("sessions.start.placeholder"))) {
                    titleInput.setText("");
                    titleInput.setForeground(Color.BLACK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (titleInput.getText().isBlank()) {
                    titleInput.setText(LangManager.get("sessions.start.placeholder"));
                    titleInput.setForeground(new Color(0x333333));
                }
            }
        });

        field.add(titleFieldLabel, BorderLayout.WEST);
        field.add(titleInput, BorderLayout.CENTER);
        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildBottom() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        timeLabel = new JLabel(LangManager.get("sessions.start.now"));
        timeLabel.setFont(new Font("Gochi Hand", Font.PLAIN, 28));
        timeLabel.setForeground(Theme.WHITE);
        timeLabel.setPreferredSize(new Dimension(150,50));

        catBtn = new JPanel();
        catBtn.setLayout(new GridBagLayout());
        catBtn.setOpaque(false);
        catBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        catBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(!isCatSelected){
                    isCatSelected = true;
                    clicked();
                } else {
                    isCatSelected = false;
                    dfl();
                }
                System.out.println("selected");
            }
        });

        // Image rx = catIcon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        // ImageIcon RX = new ImageIcon(rx);

        catLabel = new JLabel();
        catLabel.setIcon(catIcon);
        catBtn.add(catLabel);

        plusBtn = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                int d = Math.min(w, h) - 8;
                int cx = (w - d) / 2, cy = (h - d) / 2;
                g2.setColor(Theme.WHITE);
                g2.setStroke(new BasicStroke(3f));
                g2.drawOval(cx, cy, d, d);
                int mx = cx + d / 2, my = cy + d / 2, arm = d / 5;
                g2.drawLine(mx - arm, my, mx + arm, my);
                g2.drawLine(mx, my - arm, mx, my + arm);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(89, 77); }
        };
        plusBtn.setOpaque(false);

        bottom.add(timeLabel, BorderLayout.WEST);
        bottom.add(catBtn, BorderLayout.CENTER);
        bottom.add(plusBtn, BorderLayout.EAST);
        return bottom;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        float x = 2, y = 2, r = 100f;
        float w2 = w - 4, h2 = h - 4;

        Path2D.Float path = new Path2D.Float();
        path.moveTo(x + r, y);
        path.lineTo(x + w2 - r, y);
        path.quadTo(x + w2, y, x + w2, y + r);
        path.lineTo(x + w2, y + h2);
        path.lineTo(x + r, y + h2);
        path.quadTo(x, y + h2, x, y + h2 - r);
        path.lineTo(x, y + r);
        path.quadTo(x, y, x + r, y);
        path.closePath();

        g2.setColor(Theme.WHITE);
        g2.setStroke(new BasicStroke(3f));
        g2.draw(path);
        g2.dispose();
    }

    public void addStartListener(ActionListener l) {
        plusBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { l.actionPerformed(null); }
        });
        plusBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public String getSessionTitle() {
        String t = titleInput.getText();
        return t.equals(LangManager.get("sessions.start.placeholder")) ? "" : t;
    }

    public boolean getCat(){ return isCatSelected; }

    public void refreshText() {
        startLabel.setText(LangManager.get("sessions.start.label"));
        newLabel.setText(LangManager.get("sessions.start.new"));
        sessionLabel.setText(LangManager.get("sessions.start.session"));
        titleFieldLabel.setText(LangManager.get("sessions.start.titlefield"));
        timeLabel.setText(LangManager.get("sessions.start.now"));
        if (titleInput.getText().isBlank() || titleInput.getText().equals(
                LangManager.isEnglish() ? "Título de la sesión..." : "Session title...")) {
            titleInput.setText(LangManager.get("sessions.start.placeholder"));
        }
    }
}
