package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.PerformancesHandler;
import ca.ubc.cs304.database.VenuesHandler;
import ca.ubc.cs304.delegates.HomeWindowDelegate;
import ca.ubc.cs304.model.PerformancesModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateWindow extends JFrame implements ActionListener {
    HomeWindowDelegate delegate;
    PerformancesHandler pHandler;
    VenuesHandler vHandler;
    PerformancesModel[] performances;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 300;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JLabel message;
    JComboBox dropdown;
    JButton cancel;
    JButton confirm;
    String[] ids;


    public UpdateWindow(HomeWindowDelegate delegate, PerformancesHandler pHandler, VenuesHandler vHandler) {
        this.delegate = delegate;
        this.pHandler = pHandler;
        this.vHandler = vHandler;
        this.setTitle("Update an Event");
        this.setVisible(true);
        this.setLayout(new GridLayout(4, 1));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Initialize components
        message = new JLabel("select the ID of the show that you wants to update");
        message.setPreferredSize(new Dimension(350,30));
        // get IDs
        performances = pHandler.getPerformancesInfo();
        ids = new String[performances.length];
        for (int i = 0; i < performances.length; i++) {
            ids[i] = String.valueOf(performances[i].getshowid());
        }
        dropdown = new JComboBox(ids);
        dropdown.setPreferredSize(new Dimension(300,30));
        cancel = new JButton("cancel");
        cancel.addActionListener(this);
        cancel.setPreferredSize(new Dimension(100,30));
        confirm = new JButton("confirm");
        confirm.addActionListener(this);
        confirm.setPreferredSize(new Dimension(100,30));


        // Create panels
        JPanel p1 = new JPanel(new FlowLayout());
        JPanel p2 = new JPanel(new FlowLayout());
        JPanel p3 = new JPanel(new FlowLayout());
        JPanel p4 = new JPanel(new FlowLayout());
        // Add components to panels
        p2.add(message);
        p3.add(dropdown);
        p4.add(cancel);
        p4.add(confirm);



        // Add panels to the frame
        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(p4);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancel)) {
            this.dispose();
        } else {
            int id = Integer.parseInt(ids[dropdown.getSelectedIndex()]);
            PerformancesModel p = null;
            for(int i = 0; i < performances.length;i++) {
                if (performances[i].getshowid() == id) {
                    p = performances[i];
                }
            }
            UpdateWindowNext next = new UpdateWindowNext(delegate,pHandler,vHandler,p);
            this.dispose();
        }


    }
}
