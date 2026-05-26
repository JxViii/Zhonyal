package ui.pages;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import helpers.FileUtils;
import helpers.LangManager;
import ui.components.NotesViewerComp.FileButton;
import ui.components.NotesViewerComp.PDFVIewer;
import utils.Theme;

public class NotesViewer extends JPanel{

  private JPanel hero;
  private JLabel heroTitle;

  private JPanel main;

  private JPanel grid;
  private JPanel col1;
  private JPanel col2;

  private JPanel notesPanel;
  private JLabel notesLabel;

  private PDFVIewer pdfViewer;
  private JPanel fileView;
  private JLabel fileTitle;

  private List<FileButton> fileItems;
  private final String filesPath = System.getProperty("user.home") + "/.zhonyal/files";

  private File selected_file;

  public NotesViewer( File file ){
    if (file == null) return;
    this.selected_file = file;

    setLayout(new BorderLayout());
    setOpaque(false);

    hero = new JPanel(new GridBagLayout());
    hero.setOpaque(false);
    hero.setPreferredSize(new Dimension(1200,98));

    Font heroFont = new Font("BBH Bartle", Font.PLAIN, 32);

    heroTitle = new JLabel(LangManager.get("viewer.title"));
    heroTitle.setFont(heroFont);
    heroTitle.setForeground(Theme.WHITE);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10,0,0,0);
    
    hero.add(heroTitle, gbc);

    add(hero, BorderLayout.NORTH);

    //main
    
    main = new JPanel(new GridBagLayout());
    main.setOpaque(false);
    // main.setBackground(Theme.L_RED);

    grid = new JPanel(new BorderLayout(15,0));
    grid.setOpaque(false);
    Dimension dGrid = new Dimension(1100,820);
    grid.setPreferredSize(dGrid);
    grid.setMinimumSize(dGrid);
    grid.setMaximumSize(dGrid);

    // files Column;

    col1 = new JPanel();
    col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));
    col1.setOpaque(true);
    col1.setBackground(Theme.BLACK);
    Dimension dFiles = new Dimension(220,740);
    col1.setPreferredSize(dFiles);

    Font notesFont = new Font("Inter 24pt ExtraBold", Font.PLAIN, 22);

    notesPanel = new JPanel(new GridBagLayout());
    notesPanel.setOpaque(false);
    notesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
    notesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    notesLabel = new JLabel(LangManager.get("viewer.notes"));
    notesLabel.setForeground(Theme.WHITE);
    notesLabel.setFont(notesFont);

    notesPanel.add(notesLabel);

    col1.add(notesPanel);
    // col1.add(Box.createVerticalStrut(40));

    List<File> rawFiles = Arrays.asList( FileUtils.loadFiles( filesPath ) );

    fileItems = rawFiles.stream()
                .map( s -> {
                  FileButton n = new FileButton( s.getName(), s );

                  n.addActionListener( e -> {
                    this.selected_file = n.getFile();
                    n.setActive(true);
                    refreshFilesLabels();
                    fileTitle.setText(n.getFile().getName());
                    pdfViewer.loadFile(n.getFile());
                  }
                  );

                  return n;
                }
                )
                .toList();

    fileItems.forEach(
      s -> {
        if(this.selected_file.equals(s.getFile())) {
          s.setActive(true);
          s.recolor();
        }
      }   
    );

    for ( JButton n : fileItems ){

      col1.add(n);
      col1.add(Box.createVerticalStrut(20));

    }

    // PDF VIEWER

    col2 = new JPanel(new BorderLayout(0,20));
    col2.setOpaque(true);
    col2.setBackground(Theme.BLACK);

    fileView = new JPanel(new GridBagLayout());
    fileView.setOpaque(false);
    fileView.setPreferredSize(new Dimension(770, 50));

    Font fileTitleFont = new Font("Inter 24pt ExtraBold", Font.PLAIN, 20);

    fileTitle = new JLabel( selected_file.getName() );
    fileTitle.setFont(fileTitleFont);
    fileTitle.setForeground(Theme.WHITE);
    
    fileView.add(fileTitle);

    col2.add(fileView, BorderLayout.NORTH);

    pdfViewer = new PDFVIewer();
    col2.add(pdfViewer, BorderLayout.CENTER);
    pdfViewer.loadFile(selected_file);

    Border colPadding = BorderFactory.createEmptyBorder(12,15,12,15);
    col2.setBorder(colPadding);
    grid.setBorder(colPadding);
    grid.add(col1, BorderLayout.WEST);
    grid.add(col2, BorderLayout.CENTER);

    gbc.insets = new Insets(-30,0,0,0);
    main.add(grid, gbc);

    add(main, BorderLayout.CENTER);

  }



  public void refreshText() {
    if (heroTitle == null) return;
    heroTitle.setText(LangManager.get("viewer.title"));
    notesLabel.setText(LangManager.get("viewer.notes"));
  }

  public Boolean isSelected( File b ){
    return this.selected_file.equals(b);
  }

  public void refreshFilesLabels(){

    col1.removeAll();

    col1.add(notesPanel);

    fileItems.forEach(
                s -> {
                  if( !isSelected( s.getFile() ) )
                    s.setActive(false);
                  s.recolor();
                }
              );
              
    for ( JButton n : fileItems ){

      col1.add(n);
      col1.add(Box.createVerticalStrut(20));

    }

    col1.revalidate();
    col1.repaint();

  }

}
