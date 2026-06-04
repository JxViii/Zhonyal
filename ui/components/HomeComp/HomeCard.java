package ui.components.HomeComp;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Theme;

public class HomeCard extends JPanel {

  JPanel titleWrapper;
  JLabel title;
  SpacingDesc desc;
  JPanel buttonWrapper;
  TextAddButton button;

  private ActionListener listener;

  Color mainColor = Theme.LL_GREEN;
  Color secColor = Theme.L_GREEN;

  public HomeCard(
    String title_,
    String desc_,
    String buttonTitle_
  ){
    title = new JLabel(title_);

    titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0 ,0 ));
    titleWrapper.setOpaque(false);
    titleWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

    title.setForeground(mainColor);
    title.setFont(new Font("BBH Bartle", Font.PLAIN, 30));
    title.setPreferredSize(title.getPreferredSize());

    titleWrapper.add(title);

    desc = new SpacingDesc(desc_, secColor);

    button = new TextAddButton(buttonTitle_, mainColor);
    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (listener != null) listener.actionPerformed(null);
        }
    });
    // [         []          ]

    buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
    buttonWrapper.setOpaque(false);
    // buttonWrapper.setBackground(Color.green);
    buttonWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    buttonWrapper.add(button);

    addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (listener != null) listener.actionPerformed(null);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(BorderFactory.createCompoundBorder(
              BorderFactory.createLineBorder(mainColor, 6),
              BorderFactory.createEmptyBorder(40, 40, 40, 40)
            ));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!contains(e.getPoint())) {
                setBorder(BorderFactory.createCompoundBorder(
                  BorderFactory.createLineBorder(mainColor, 3),
                  BorderFactory.createEmptyBorder(40, 40, 40, 40)
                ));
            }
        }
    });

    this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(mainColor, 3),
      BorderFactory.createEmptyBorder(40, 40, 40, 40)
    ));

    this.add(titleWrapper);
    this.add(Box.createVerticalStrut(20));
    this.add(desc);
    this.add(Box.createVerticalStrut(20));
    this.add(buttonWrapper);

    this.setOpaque(false);
    this.setPreferredSize(new Dimension(410, 373));
    
  }

  public void addActionListener(ActionListener l) { this.listener = l; }

  public void setTitle(String t) { title.setText(t); }
  public void setDesc(String d) { desc.setText(d); }

  // @Override
  // public void addNotify() {
  //     super.addNotify();
  //     // Runs after layout, sizes are real
  //     SwingUtilities.invokeLater(() -> {
  //         System.out.println("HomePage: " + getWidth() + " x " + getHeight());
  //     });
  // }

}
