import java.awt.CardLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import helpers.LangManager;
import helpers.Session;
import helpers.UserProfile;
import ui.components.AppPopup;
import ui.pages.HomePage;
import ui.pages.Notes;
import ui.pages.NotesViewer;
import ui.pages.OnboardingPage;
import ui.pages.SessionDetails;
import ui.pages.SessionsHomePage;
import ui.pages.SessionsManager;
import ui.templates.AppShell;

public class App extends JFrame {

    private final CardLayout rootLayout = new CardLayout();
    private final JPanel root = new JPanel(rootLayout);
    private final AppShell shell = new AppShell();
    private OnboardingPage onboarding;

    private SessionsManager sessionsManager = new SessionsManager();
    private SessionDetails sessionDetails = new SessionDetails(null);
    private SessionsHomePage sessionsHome = new SessionsHomePage();
    private Notes notes = new Notes();
    private NotesViewer notesViewer = new NotesViewer(null);
    private HomePage home;

    private Session currentSession = null;

    public App() {
        setTitle("Zhonyal");
        setIconImage(new javax.swing.ImageIcon("images/Logo.png").getImage());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1353, 929);
        setResizable(false);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println(getWidth() + " " + getHeight());
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (sessionsHome.isChronoRunning()) {
                    AppPopup.confirm(App.this, LangManager.get("dialog.session.title"),
                        LangManager.get("dialog.session.body"),
                        () -> System.exit(0));
                } else {
                    System.exit(0);
                }
            }
        });

        home = new HomePage();
        onboarding = new OnboardingPage();

        home.setOnNavigate(page -> navigate(page));
        sessionsHome.setOnNavigate(page -> navigate(page));
        sessionsManager.setOnSessionSelect(s -> navigateToSession(s));
        notes.setOnFileSelect(f -> navigateToFile(f));
        shell.getSidebar().setOnReset(() -> notes.refreshFiles());
        shell.getSidebar().setOnProfile(() -> openProfilePage());
        
        shell.getContentArea().add(home, "home");
        shell.getContentArea().add(sessionsHome, "sessions");
        shell.getContentArea().add(notes, "notes");
        shell.getContentArea().add(notesViewer, "notes-viewer");
        shell.getContentArea().add(sessionsManager, "sessions-manager");
        shell.getContentArea().add(sessionDetails, "session-details");

        onboarding.setOnComplete(() -> { home.refresh(); shell.getSidebar().refreshLabels(); navigateRoot("app"); });
        root.add(onboarding, "onboarding");
        root.add(shell, "app");
        rootLayout.show(root, UserProfile.exists() ? "app" : "onboarding");

        LangManager.setOnChange(() -> {
            home.refresh();
            notes.refreshText();
            notesViewer.refreshText();
            sessionsHome.refreshText();
            shell.getSidebar().refreshLabels();

            boolean onManager = sessionsManager.isShowing();
            boolean onDetails = sessionDetails.isShowing();

            SessionsManager freshManager = new SessionsManager();
            freshManager.setOnSessionSelect(s -> navigateToSession(s));
            shell.replaceCard(sessionsManager, freshManager, "sessions-manager");
            sessionsManager = freshManager;

            if (currentSession != null) {
                SessionDetails freshDetails = new SessionDetails(currentSession);
                freshDetails.setOnBack(() -> navigate("sessions-manager"));
                shell.replaceCard(sessionDetails, freshDetails, "session-details");
                sessionDetails = freshDetails;
            }

            if (onManager) shell.navigate("sessions-manager", new ui.templates.Page("Sessions"));
            else if (onDetails) shell.navigate("session-details", new ui.templates.Page("Sessions"));
        });

        add(root);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void navigateRoot(String page) {
        rootLayout.show(root, page);
    }

    private void openProfilePage() {
        root.remove(onboarding);
        onboarding = new OnboardingPage();
        onboarding.setOnComplete(() -> {
            home.refresh();
            shell.getSidebar().refreshLabels();
            navigateRoot("app");
        });
        root.add(onboarding, "onboarding");
        rootLayout.show(root, "onboarding");
    }

    public void navigate(String page) {
        if (page.equals("sessions-manager")) {
            SessionsManager fresh = new SessionsManager();
            fresh.setOnSessionSelect(s -> navigateToSession(s));
            shell.replaceCard(sessionsManager, fresh, "sessions-manager");
            sessionsManager = fresh;
        }
        boolean sessions = page.startsWith("session");
        shell.navigate(page, new ui.templates.Page(sessions ? "Sessions" : "Default"));
    }

    public void navigateToFile(File f) {
        NotesViewer fresh = new NotesViewer(f);
        shell.replaceCard(notesViewer, fresh, "notes-viewer");
        notesViewer = fresh;
        shell.navigate("notes-viewer", new ui.templates.Page("Default"));
    }

    public void navigateToSession(Session s) {
        currentSession = s;
        SessionDetails fresh = new SessionDetails(s);
        fresh.setOnBack(() -> navigate("sessions-manager"));
        shell.replaceCard(sessionDetails, fresh, "session-details");
        sessionDetails = fresh;
        shell.navigate("session-details", new ui.templates.Page("Sessions"));
    }

}
