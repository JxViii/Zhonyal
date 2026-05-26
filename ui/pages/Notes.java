package ui.pages;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import helpers.FileUtils;
import helpers.LangManager;
import ui.components.AppPopup;
import ui.components.NotesComp.AddFileButton;
import ui.components.NotesComp.FileItem;
import utils.Theme;

public class Notes extends JPanel {

  private JPanel hero;
  private JPanel filesPanel;

  private JLabel title;
  private JLabel arrow;
  private AddFileButton add;

  private JPanel grid;
  private JButton deleteButton;
  private List<FileItem> fileItems;
  private boolean isDelete = false;
  private final String filesPath = System.getProperty("user.home") + "/.zhonyal/files";
  private Consumer<File> onFileSelect;

  public void setOnFileSelect(Consumer<File> handler) { this.onFileSelect = handler; }

  public Notes(){

    setLayout(new BorderLayout());
    setOpaque(false);

    hero = new JPanel();
    hero.setLayout(new FlowLayout(FlowLayout.CENTER,85,0));
    hero.setOpaque(false);

    Border heroPadding = BorderFactory.createEmptyBorder(40,40,40,40);
    Font titleFont = new Font("BBH Bartle", Font.PLAIN, 42);

    hero.setBorder(heroPadding);

    title = new JLabel(LangManager.get("notes.title"));
    title.setForeground(Theme.WHITE);
    title.setFont(titleFont);

    ImageIcon arrPng = new ImageIcon("images/notes-arrow.png");
    Image arrPx = arrPng.getImage().getScaledInstance(27, 180,Image.SCALE_SMOOTH);
    arrPng = new ImageIcon(arrPx);

    arrow = new JLabel();
    arrow.setIcon(arrPng);

    add = new AddFileButton( () -> refreshFiles() );

    hero.add(title);
    hero.add(arrow);
    hero.add(add);

    add(hero, BorderLayout.NORTH);

    //grid

    filesPanel = new JPanel(new GridBagLayout());
    filesPanel.setOpaque(false);

    grid = new JPanel(new FlowLayout( FlowLayout.LEADING, 20, 20));
    grid.setOpaque(false);
    grid.setPreferredSize(new Dimension(1010,420));

    JPanel deletePanel = new JPanel(new FlowLayout( FlowLayout.TRAILING ));
    deletePanel.setOpaque(false);
    deletePanel.setPreferredSize(new Dimension(1010,40));

    Font delFont = new Font("Inter 18pt", Font.BOLD, 15);

    deleteButton = new JButton(LangManager.get("notes.delete"));
    deleteButton.setOpaque(false);
    deleteButton.setContentAreaFilled(false);
    deleteButton.setBorderPainted(false);
    deleteButton.setFocusPainted(false);
    deleteButton.setPreferredSize(new Dimension(150, 36));
    deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    deleteButton.setFont(delFont);
    deleteButton.setBorder(
      BorderFactory.createLineBorder(Theme.WHITE, 2)
    );
    deleteButton.setForeground(Theme.WHITE);
    deleteButton.addActionListener( e -> {

      isDelete = !isDelete;
      
      deleteButton.setText(isDelete ? LangManager.get("notes.viewer") : LangManager.get("notes.delete"));

      refreshFiles();

    });

    refreshFiles();

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridy=0;
    gbc.insets = new Insets(0,0,0,0);

    filesPanel.add(grid, gbc);

    gbc.gridy=1;
    gbc.insets = new Insets(0,0,0,0);

    deletePanel.add(deleteButton);
    filesPanel.add(deletePanel, gbc);

    add(filesPanel, BorderLayout.CENTER);

  }

  public void refreshText() {
    title.setText(LangManager.get("notes.title"));
    deleteButton.setText(isDelete ? LangManager.get("notes.viewer") : LangManager.get("notes.delete"));
    add.refreshText();
  }

  public void refreshFiles(){

    grid.removeAll();

    List<File> arr = Arrays.asList(FileUtils.loadFiles(filesPath));

    fileItems = arr.stream()
                  .map( s -> new FileItem(
                      s,
                      isDelete,
                      f -> { if (onFileSelect != null) onFileSelect.accept(f); },
                      () -> AppPopup.confirm(Notes.this, LangManager.get("dialog.delete.title"),
                              LangManager.format("dialog.delete.body", s.getName()),
                              () -> { s.delete(); refreshFiles(); })
                  ))
                  .toList();
                  
    for( FileItem f : fileItems ){
      grid.add(f);
    }

    repaint();
    revalidate();

  }

}
