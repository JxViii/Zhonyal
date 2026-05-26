package ui.components.SessionsManagerComp;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import helpers.LangManager;
import utils.Theme;

public class DatePicker extends JPanel {

    private LocalDate selected;
    private LocalDate viewing;
    private JButton displayBtn;
    private JPopupMenu popup;
    private CalendarPanel calPanel;
    private Runnable onRefresh;

    private static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofPattern("dd / MM / yyyy");

    public DatePicker( Runnable onRefresh ) {

        this.onRefresh = onRefresh;

        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        viewing = LocalDate.now().withDayOfMonth(1);

        displayBtn = new JButton("DD / MM / YYYY");
        displayBtn.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 16));
        displayBtn.setForeground(Theme.L_GREY);
        displayBtn.setBackground(Theme.BLACK);
        displayBtn.setOpaque(true);
        displayBtn.setFocusPainted(false);
        displayBtn.setContentAreaFilled(true);
        displayBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        displayBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.LL_GREY, 1),
            new EmptyBorder(6, 14, 6, 14)
        ));
        displayBtn.addActionListener(e -> {
            calPanel.refresh();
            popup.show(displayBtn, 0, displayBtn.getHeight() + 4);
        });
        displayBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { displayBtn.setForeground(Theme.WHITE); }
            @Override public void mouseExited(MouseEvent e)  { displayBtn.setForeground(selected == null ? Theme.L_GREY : Theme.WHITE); }
        });

        add(displayBtn);
        buildPopup();
    }

    private void buildPopup() {
        popup = new JPopupMenu();
        popup.setBackground(Theme.BLACK);
        popup.setBorder(BorderFactory.createLineBorder(Theme.LOGOUT_BUTTON, 1));

        calPanel = new CalendarPanel();
        popup.add(calPanel);
    }

    void pick(LocalDate date) {
        selected = date;
        displayBtn.setText(date.format(DISPLAY));
        displayBtn.setForeground(Theme.WHITE);
        popup.setVisible(false);
        onRefresh.run();
    }

    public LocalDate getSelected() { return selected; }

    public void clear() {
        selected = null;
        displayBtn.setText("DD / MM / YYYY");
        displayBtn.setForeground(Theme.L_GREY);
    }

    private class CalendarPanel extends JPanel {

        private JLabel monthLabel;
        private JPanel daysGrid;

        CalendarPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Theme.BLACK);
            setBorder(new EmptyBorder(12, 14, 12, 14));
            setPreferredSize(new Dimension(252, 230));

            // header
            JPanel header = new JPanel(new java.awt.BorderLayout());
            header.setOpaque(false);
            header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

            JButton prev = nav("<");
            JButton next = nav(">");
            prev.addActionListener(e -> { viewing = viewing.minusMonths(1); refresh(); });
            next.addActionListener(e -> { viewing = viewing.plusMonths(1); refresh(); });

            monthLabel = new JLabel("", JLabel.CENTER);
            monthLabel.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 16));
            monthLabel.setForeground(Theme.WHITE);

            header.add(prev, java.awt.BorderLayout.WEST);
            header.add(monthLabel, java.awt.BorderLayout.CENTER);
            header.add(next, java.awt.BorderLayout.EAST);

            // weekday row
            JPanel weekdays = new JPanel(new GridLayout(1, 7, 4, 0));
            weekdays.setOpaque(false);
            weekdays.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
            weekdays.setBorder(new EmptyBorder(8, 0, 4, 0));
            for (String d : LangManager.get("filter.days").split(",")) {
                JLabel l = new JLabel(d, JLabel.CENTER);
                l.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 14));
                l.setForeground(Theme.L_GREY);
                weekdays.add(l);
            }

            daysGrid = new JPanel(new GridLayout(6, 7, 4, 4));
            daysGrid.setOpaque(false);

            add(header);
            add(weekdays);
            add(daysGrid);

            refresh();
        }

        private JButton nav(String text) {
            JButton b = new JButton(text);
            b.setFont(new Font("Inter 24pt ExtraBold", Font.PLAIN, 16));
            b.setForeground(Theme.L_GREY);
            b.setBackground(Theme.BLACK);
            b.setBorder(new EmptyBorder(2, 8, 2, 8));
            b.setFocusPainted(false);
            b.setContentAreaFilled(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { b.setForeground(Theme.WHITE); }
                @Override public void mouseExited(MouseEvent e)  { b.setForeground(Theme.L_GREY); }
            });
            return b;
        }

        void refresh() {
            monthLabel.setText(viewing.format(DateTimeFormatter.ofPattern("MMMM yyyy", LangManager.getLocale())).toUpperCase());
            daysGrid.removeAll();

            int firstDow = viewing.getDayOfWeek().getValue(); // 1=Mon
            int daysInMonth = YearMonth.of(viewing.getYear(), viewing.getMonth()).lengthOfMonth();

            for (int i = 1; i < firstDow; i++) daysGrid.add(new JLabel());

            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate date = viewing.withDayOfMonth(day);
                daysGrid.add(dayBtn(day, date));
            }

            int filled = firstDow - 1 + daysInMonth;
            int rem = filled % 7 == 0 ? 0 : 7 - (filled % 7);
            for (int i = 0; i < rem; i++) daysGrid.add(new JLabel());

            daysGrid.revalidate();
            daysGrid.repaint();
        }

        private JButton dayBtn(int day, LocalDate date) {
            boolean isSel   = date.equals(selected);
            boolean isToday = date.equals(LocalDate.now());

            JButton b = new JButton(String.valueOf(day)) {
                @Override protected void paintComponent(Graphics g) {
                    if (isSel) {
                        g.setColor(Theme.CYAN);
                        g.fillOval(1, 1, getWidth() - 2, getHeight() - 2);
                    } else if (isToday) {
                        g.setColor(Theme.LOGOUT_BUTTON);
                        g.fillOval(1, 1, getWidth() - 2, getHeight() - 2);
                    }
                    super.paintComponent(g);
                }
            };
            b.setFont(new Font("Inter 18pt", Font.PLAIN, 12));
            b.setForeground(isSel ? Theme.BLACK : Theme.WHITE);
            b.setBorder(new EmptyBorder(2, 2, 2, 2));
            b.setFocusPainted(false);
            b.setContentAreaFilled(false);
            b.setOpaque(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setHorizontalAlignment(JButton.CENTER);
            b.addActionListener(e -> pick(date));
            b.addMouseListener(new MouseAdapter() {
                Color original = b.getForeground();
                @Override public void mouseEntered(MouseEvent e) { if (!isSel) b.setForeground(Theme.CYAN); }
                @Override public void mouseExited(MouseEvent e)  { b.setForeground(original); }
            });
            return b;
        }
    }
}
