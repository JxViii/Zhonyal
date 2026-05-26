package ui.components.NotesComp;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import helpers.FileUtils;
import helpers.LangManager;
import utils.Theme;

public class AddFileButton extends JPanel {

    private JPanel plusBtn;
    private JLabel addLabel;
    private JLabel newLabel;
    private JLabel fileLabel;

    public AddFileButton( Runnable onRefresh ) {
        setLayout(new BorderLayout());
        setOpaque(false);
        Dimension size = new Dimension(420, 140);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setBorder(new EmptyBorder(18, 40, 18, 40));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                FileUtils.selectFile( onRefresh );
            }
        });

        add(buildTextBlock(), BorderLayout.CENTER);
        add(buildPlusBtn(), BorderLayout.EAST);
    }

    private JPanel buildTextBlock() {
        JPanel block = new JPanel();
        block.setLayout(new GridBagLayout());
        block.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        row1.setOpaque(false);
        row1.setAlignmentX(LEFT_ALIGNMENT);

        addLabel = new JLabel(LangManager.get("notes.add"));
        addLabel.setFont(new Font("BBH Bartle", Font.PLAIN, 32));
        addLabel.setForeground(Theme.WHITE);

        newLabel = new JLabel(LangManager.get("notes.add.new"));
        newLabel.setFont(new Font("Gochi Hand", Font.PLAIN, 43));
        newLabel.setForeground(Theme.LL_GREEN);

        row1.add(addLabel);
        row1.add(newLabel);

        fileLabel = new JLabel(LangManager.get("notes.add.file"));
        fileLabel.setFont(new Font("BBH Bartle", Font.PLAIN, 32));
        fileLabel.setForeground(Theme.WHITE);
        fileLabel.setAlignmentX(LEFT_ALIGNMENT);

        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0,-50,0,0);
        block.add(row1, gbc);

        gbc.insets = new Insets(0,-40,0,0);
        gbc.gridy = 1;
        block.add(fileLabel, gbc);

        return block;
    }

    private JPanel buildPlusBtn() {
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
                g2.setStroke(new BasicStroke(5f));
                g2.drawOval(cx, cy, d, d);
                int mx = cx + d / 2, my = cy + d / 2, arm = d / 5;
                g2.drawLine(mx - arm, my, mx + arm, my);
                g2.drawLine(mx, my - arm, mx, my + arm);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(58, 58); }
        };
        plusBtn.setOpaque(false);
        return plusBtn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        int r = 50;
        g2.setColor(Theme.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(1, 1, w - 2, h - 2, r, r);
        g2.dispose();
    }

    public void addClickListener(ActionListener l) {
        plusBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { l.actionPerformed(null); }
        });
    }

    public void refreshText() {
        addLabel.setText(LangManager.get("notes.add"));
        newLabel.setText(LangManager.get("notes.add.new"));
        fileLabel.setText(LangManager.get("notes.add.file"));

        if( !LangManager.isEnglish() ){
            Dimension size = new Dimension(480, 140);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
        }
        else{
            Dimension size = new Dimension(420, 140);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
        }
    }
}
