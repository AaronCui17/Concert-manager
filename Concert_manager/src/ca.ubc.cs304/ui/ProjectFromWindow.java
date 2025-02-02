package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.PerformancesHandler;
import ca.ubc.cs304.delegates.HomeWindowDelegate;
import ca.ubc.cs304.model.namesModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectFromWindow extends JFrame implements ActionListener {
    HomeWindowDelegate delegate;
    PerformancesHandler pHandler;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 300;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JLabel message;
    JComboBox dropdown;
    JButton cancel;
    JButton confirm;
    namesModel[] namesModels;
    String[] names;

    public ProjectFromWindow(HomeWindowDelegate delegate, PerformancesHandler pHandler) {
        this.delegate = delegate;
        this.pHandler = pHandler;
        this.setTitle("Choose Information");
        this.setVisible(true);
        this.setLayout(new GridLayout(4, 1));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);
        message = new JLabel("Select the item that you would like to know about");
        namesModels = pHandler.getTableNames();
        names = new String[namesModels.length];
        for (int i = 0; i < namesModels.length; i++) {
            names[i] = namesModels[i].getName();
        }
        dropdown = new JComboBox(names);
        dropdown.setPreferredSize(new Dimension(350,30));
        cancel = new JButton("cancel");
        cancel.addActionListener(this);
        confirm = new JButton("confirm");
        confirm.addActionListener(this);

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();

        p2.add(message);
        p2.add(dropdown);
        p4.add(cancel);
        p4.add(confirm);


        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(p4);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(confirm)) {
            int i = dropdown.getSelectedIndex();
            String tableName = names[i];
            new ProjectionPopUpWindow(delegate,pHandler,tableName);
        } else {
            this.dispose();
        }
    }
}
