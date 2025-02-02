package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.PerformancesHandler;
import ca.ubc.cs304.delegates.HomeWindowDelegate;
import ca.ubc.cs304.model.ProjectionModel;
import ca.ubc.cs304.model.namesModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectionPopUpWindow extends JFrame implements ActionListener {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JScrollPane scroll;
    String[] information;
    JButton cancel;
    JButton show;
    String tableName;
    JCheckBox[] checkBoxes;
    namesModel[] attributesModels;
    String[] attributes;
    PerformancesHandler pHandler;
    public ProjectionPopUpWindow(HomeWindowDelegate delegate, PerformancesHandler pHandler, String tableName) {
        this.tableName = tableName;
        this.pHandler = pHandler;
        this.setTitle(tableName);
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x,y,WINDOW_WIDTH,WINDOW_HEIGHT);
        this.setLayout(new GridLayout(3,1));
        //button
        cancel = new JButton("cancel");
        cancel.addActionListener(this);
        show = new JButton("show");
        show.addActionListener(this);

        attributesModels = pHandler.getColumnNames(tableName);
        attributes = new String[attributesModels.length];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = attributesModels[i].getName();
        }
        checkBoxes = new JCheckBox[attributes.length];
        for(int i = 0; i < attributes.length; i++) {
            checkBoxes[i] = new JCheckBox(attributes[i]);
        }


        scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(600, 200));


        // pane
        JPanel p1 = new JPanel(new FlowLayout());
        JPanel p2 = new JPanel(new FlowLayout());
        JPanel p3 = new JPanel(new FlowLayout());

          p1.add(scroll);

          for (int i = 0; i < attributes.length; i++) {
              p2.add(checkBoxes[i]);
          }
        p3.add(cancel);
        p3.add(show);
        this.add(p1);
        this.add(p2);
        this.add(p3);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancel)) {
            this.dispose();
        } else {
            String columns = "";
            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isSelected()) {
                    columns += attributes[i] + ", ";
                }
            }
            if (columns.equals("")) {
                return;
            } else {
                columns = columns.substring(0, columns.length() - 2);
                ProjectionModel[] result = pHandler.getProjectionInfo(tableName, columns);
                updateScrollPaneContent(result);
            }

        }


    }

    private void updateScrollPaneContent(ProjectionModel[] result) {
        JTextArea textArea = new JTextArea();
        for (ProjectionModel model : result) {
            textArea.append(model.getattributes() + "\n");
        }
        scroll.setViewportView(textArea);
    }
}
