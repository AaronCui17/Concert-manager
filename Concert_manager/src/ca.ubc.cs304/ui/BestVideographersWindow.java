package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.FilmHandler;
import ca.ubc.cs304.model.VideographersModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BestVideographersWindow extends JFrame implements ActionListener {
    private static final int WINDOW_WIDTH = 450;
    private static final int WINDOW_HEIGHT = 300;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JScrollPane scroll;
    JLabel msg;
    JButton back;
    JList videographers;
    public BestVideographersWindow(FilmHandler fHandler) {
        back = new JButton("back");
        back.addActionListener(this);
        this.setTitle("Best Videographers");
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x,y,WINDOW_WIDTH,WINDOW_HEIGHT);
        this.setLayout(new GridLayout(2,1));
        VideographersModel[] vms = fHandler.getDivisonInfo();
        String[] information = new String[vms.length];
        for (int i = 0; i < vms.length; i++) {
            information[i] = "videographer ID: " + vms[i].getVid() + ", Videographer Name: " + vms[i].getvName();
        }
        videographers = new JList<>(information);
        scroll = new JScrollPane(videographers);
        scroll.setPreferredSize(new Dimension(350, 100));
        msg = new JLabel("The following videographer(s) participated in filming all events");
        msg.setPreferredSize(new Dimension(400,30));
        msg.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        p1.add(msg);
        p1.add(scroll);
        p2.add(back);
        this.add(p1);
        this.add(p2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }
}
