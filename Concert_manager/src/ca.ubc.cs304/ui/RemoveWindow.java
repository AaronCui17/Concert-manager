package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.PerformancesHandler;
import ca.ubc.cs304.delegates.HomeWindowDelegate;
import ca.ubc.cs304.model.PerformancesModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveWindow extends JFrame implements ActionListener {
    private static final int WINDOW_WIDTH = 500;
    HomeWindowDelegate delegate;
    private static final int WINDOW_HEIGHT = 300;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JLabel message;
    JTextField name;
    JButton cancel;
    JButton remove;
    PerformancesModel[] performances;
    PerformancesHandler pHandler;

    public RemoveWindow(HomeWindowDelegate delegate, PerformancesHandler pHandler) {
        this.delegate = delegate;
        this.pHandler = pHandler;
        this.setTitle("Remove an Event");
        this.setVisible(true);
        this.setLayout(new GridLayout(4, 1));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Initialize components
        message = new JLabel("Show name: ");
        message.setPreferredSize(new Dimension(100,30));
        name = new JTextField();
        name.setPreferredSize(new Dimension(150,30));
        cancel = new JButton("cancel");
        cancel.addActionListener(this);
        cancel.setPreferredSize(new Dimension(100,30));
        remove = new JButton("remove");
        remove.addActionListener(this);
        remove.setPreferredSize(new Dimension(100,30));
        performances = pHandler.getPerformancesInfo();


        // Create panels
        JPanel p1 = new JPanel(new FlowLayout());
        JPanel p2 = new JPanel(new FlowLayout());
        JPanel p3 = new JPanel(new FlowLayout());
        JPanel p4 = new JPanel(new FlowLayout());
        // Add components to panels
        p2.add(message);
        p2.add(name);
        p4.add(cancel);
        p4.add(remove);



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
        }else {
            if (name.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a valid name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                boolean inPerformances = false;
                for (int i = 0; i < performances.length; i++) {
                    if (performances[i].getsName().equals(name.getText())) {
                        inPerformances = true;
                    }
                }

                if (inPerformances) {
                    delegate.deletePerformances(name.getText());
                    performances = pHandler.getPerformancesInfo();
                    JOptionPane.showMessageDialog(this, "Events with the given name are removed");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "We do not see any performances with the given name", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }

    }
}
