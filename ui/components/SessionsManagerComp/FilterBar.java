package ui.components.SessionsManagerComp;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import helpers.LangManager;
import utils.Theme;

public class FilterBar extends JPanel {

    private JButton newest;
    private JButton oldest;
    private JButton reset;
    private DatePicker dateFrom;
    private DatePicker dateTo;
    private String activeFilter = "newest";
    private Runnable onRefresh;

    public FilterBar( Runnable onRefresh ) {

        this.onRefresh = onRefresh;

        setLayout(new FlowLayout(FlowLayout.LEFT, 14, 0));
        setOpaque(false);
        setPreferredSize(new Dimension(980, 46));

        JLabel label = new JLabel(LangManager.get("filter.label"));
        label.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 16));
        label.setForeground(Theme.L_GREY);

        newest = filterBtn(LangManager.get("filter.newest"));
        oldest = filterBtn(LangManager.get("filter.oldest"));
        reset = resetBtn(LangManager.get("filter.reset"));
        dateFrom = new DatePicker(this.onRefresh);
        dateTo   = new DatePicker(this.onRefresh);

        JLabel arrow = new JLabel("→");
        arrow.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 16));
        arrow.setForeground(Theme.L_GREY);

        newest.addActionListener(e -> {
            setActive("newest");
        });
        oldest.addActionListener(e -> {
            setActive("oldest");
        });

        setActive("newest");

        add(label);
        add(newest);
        add(oldest);
        add(dateFrom);
        add(arrow);
        add(dateTo);
        add(reset);
    }

    private JButton filterBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 16));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.addActionListener( e -> onRefresh.run() );

        return b;
    }

    private JButton resetBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 16));
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setContentAreaFilled(false);
        b.setForeground(Theme.WHITE);
        b.setOpaque(false);
        b.addActionListener( e -> {
            
            dateFrom.clear();
            dateTo.clear();

            onRefresh.run();
        });

        return b;
    }

    private void setActive(String filter) {
        activeFilter = filter;
        style(newest, filter.equals("newest"));
        style(oldest, filter.equals("oldest"));
    }

    private void style(JButton b, boolean active) {
        if (active) {
            b.setForeground(Theme.BLACK);
            b.setOpaque(true);
            b.setContentAreaFilled(true);
            b.setBackground(Theme.LL_GREEN);
            b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.LL_GREEN, 1),
                new EmptyBorder(6, 14, 6, 14)
            ));
        } else {
            b.setForeground(Theme.WHITE);
            b.setOpaque(false);
            b.setContentAreaFilled(false);
            b.setBackground(Theme.BLACK);
            b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.LOGOUT_BUTTON, 1),
                new EmptyBorder(6, 14, 6, 14)
            ));
        }
    }
    
    public String getActiveFilter() { return activeFilter; }
    public LocalDate getDateFrom() { return dateFrom.getSelected(); }
    public LocalDate getDateTo()   { return dateTo.getSelected(); }
}
