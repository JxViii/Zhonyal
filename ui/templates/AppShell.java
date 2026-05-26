package ui.templates;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class AppShell extends JPanel {

    private final CardLayout contentLayout = new CardLayout();
    private final JPanel contentArea = new JPanel(contentLayout);
    private final Header header;
    private final Sidebar sidebar;

    private final Image sessionsBg = new ImageIcon("images/sessions-bg.png").getImage();
    private final Image greenBg    = new ImageIcon("images/green-bg.png").getImage();
    private Image currentBg = greenBg;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(currentBg, 0, 0, getWidth(), getHeight(), this);
    }

    public void setPageBg(boolean sessions) {
        currentBg = sessions ? sessionsBg : greenBg;
        repaint();
    }

    public AppShell() {
        setLayout(new BorderLayout());
        setOpaque(true);

        contentArea.setOpaque(false);

        sidebar = new Sidebar();
        header = new Header(new Page("Default"), () -> sidebar.toggle());

        sidebar.setOnNavigate(key -> {
            boolean sessions = key.startsWith("session");
            navigate(key, new Page(sessions ? "Sessions" : "Default"));
        });

        add(header, BorderLayout.NORTH);
        add(sidebar, BorderLayout.EAST);
        add(contentArea, BorderLayout.CENTER);
    }

    public JPanel getContentArea() { return contentArea; }
    public Sidebar getSidebar() { return sidebar; }

    public void navigate(String page, Page p) {
        setPageBg(page.startsWith("session"));
        contentLayout.show(contentArea, page);
        header.setPage(p);
        sidebar.setActive(page);
    }

    public void replaceCard(JPanel old, JPanel next, String key) {
        contentArea.remove(old);
        contentArea.add(next, key);
    }

}
