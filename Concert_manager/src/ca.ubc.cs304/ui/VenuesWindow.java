package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.PerformancesHandler;
import ca.ubc.cs304.database.VenuesHandler;
import ca.ubc.cs304.delegates.HomeWindowDelegate;
import ca.ubc.cs304.model.NestedAggregationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VenuesWindow extends JFrame implements ActionListener {
    HomeWindowDelegate delegate;
    PerformancesHandler pHandler;
    VenuesHandler vHandler;
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 300;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JLabel message;
    JComboBox dropdown;
    JButton cancel;
    JButton confirm;
    NestedAggregationModel[] venues;

    public VenuesWindow(HomeWindowDelegate delegate, VenuesHandler vHandler, PerformancesHandler pHandler) {
        this.delegate = delegate;
        this.pHandler = pHandler;
        this.vHandler = vHandler;
        this.setTitle("Average Number of Performers Per Performance in a Venue");
        this.setVisible(true);
        this.setLayout(new GridLayout(4, 1));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Initialize components
        message = new JLabel("<html>See the average number of performers per performance in the selected venue <br>" +
                "Note: the following list only contains venues that held more than one performance </html>");
        message.setPreferredSize(new Dimension(600,60));

        venues = pHandler.getNestedAggregationInfo();
        String[] addresses = new String[venues.length];
        for (int i = 0; i < venues.length; i++) {
            addresses[i] = venues[i].getsAddress();
        }

        dropdown = new JComboBox(addresses);
        dropdown.setPreferredSize(new Dimension(600,30));
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
            int avg = venues[dropdown.getSelectedIndex()].getAvgNumPerformers();
            JOptionPane.showMessageDialog(this, "The average amount of performers per performances in this venues is: " + avg);
            this.dispose();
        }

    }
}
