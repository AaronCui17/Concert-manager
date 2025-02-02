package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.VideographersHandler;
import ca.ubc.cs304.delegates.HomeWindowDelegate;
import ca.ubc.cs304.model.GroupByModel;
import ca.ubc.cs304.model.VideographersModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddVideographerWindow extends JFrame implements ActionListener {
    HomeWindowDelegate delegate;
    VideographersHandler vidHandler;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 300;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JLabel message;
    JTextField name;
    JButton cancel;
    JButton regVid;
    GroupByModel[] datesAndCount;

    public AddVideographerWindow(HomeWindowDelegate delegate, VideographersHandler vidHandler) {
        this.delegate = delegate;
        this.vidHandler = vidHandler;
        this.setTitle("Register a Videographer");
        this.setVisible(true);
        this.setLayout(new GridLayout(4, 1));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);
        message = new JLabel("Enter the name of the videographer: ");
        name = new JTextField();
        name.setPreferredSize(new Dimension(350,30));
        cancel = new JButton("cancel");
        cancel.addActionListener(this);
        regVid = new JButton("register videographer");
        regVid.addActionListener(this);

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();

        p2.add(message);
        p2.add(name);
        p4.add(cancel);
        p4.add(regVid);


        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(p4);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(regVid)) {
            if (name.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid name");
                return;
            } else {
                int id = HomeWindow.vidIdCount;
                HomeWindow.vidIdCount++;
                VideographersModel vid = new VideographersModel(id, name.getText());
                vidHandler.insertVideographers(vid);
                JOptionPane.showMessageDialog(this,"Videographer: " + name.getText() + " has been added, videographer ID is: " + id);
                this.dispose();
            }
        } else {
            this.dispose();
        }

    }
}
